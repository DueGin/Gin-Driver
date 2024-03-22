package com.ginDriver.main.file;


import com.ginDriver.core.domain.bo.UserBO;
import com.ginDriver.main.domain.po.Md5File;
import com.ginDriver.main.file.domain.dto.ChunkDTO;
import com.ginDriver.main.file.upload.FileUploadHandleFactory;
import com.ginDriver.main.file.upload.FileUploadHandler;
import com.ginDriver.main.security.utils.SecurityUtils;
import com.ginDriver.main.file.constants.UploadHandleType;
import com.ginDriver.main.file.constants.UploadStatus;
import com.ginDriver.main.file.domain.po.File;
import com.ginDriver.main.file.download.FileDownloadHandle;
import com.ginDriver.main.file.download.IFileDownloadHandle;
import com.ginDriver.main.file.domain.po.Chunk;
import com.ginDriver.main.file.domain.po.FileChunk;
import com.ginDriver.main.file.exception.UnLoginException;
import com.ginDriver.main.service.FileService;
import com.ginDriver.main.service.Md5FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FileManager {

    private FileUploadHandler fileUploadHandler;

    private IFileDownloadHandle fileDownloadHandle;

    public static String storagePath;

    @Resource
    private Environment environment;

    @Resource
    private Md5FileService md5FileService;

    @Resource
    private FileService fileService;

    // 已上传的分片文件集合<userId, <fileName, [..分片DTO]>>
    public final static Map<Long, Map<String, List<ChunkDTO>>> uploaded = new ConcurrentHashMap<>();

    // 已合并的文件集合<userId, <fileName, [..文件]>>
    public final static Map<Long, Map<String, File>> wholeFiles = new ConcurrentHashMap<>();

    // 前端传来正确的md5
    public final static Map<Long, List<ChunkDTO>> md5Map = new ConcurrentHashMap<>();

    /**
     * <upload_token, {upload_token, md5, exist}>
     */
    public final static Map<String, Map<String, Object>> PRE_CHECK_MAP = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        storagePath = environment.getProperty("file.upload.location.storagePath");
    }

    public FileManager() {
        this.fileUploadHandler = FileUploadHandleFactory.getFileUploadHandle(UploadHandleType.LOCATION);
        this.fileDownloadHandle = new FileDownloadHandle();
    }

    public Map<String, Object> isExist(String md5) {
        Md5File md5File = md5FileService.lambdaQuery().eq(Md5File::getMd5, md5).one();
        Map<String, Object> map = new HashMap<>();
        map.put("md5", md5);
        String uploadToken = UUID.randomUUID().toString();
        map.put("upload_token", uploadToken);
        map.put("exist", md5File != null ? 1 : 0);
        PRE_CHECK_MAP.put(uploadToken, map);
        return map;
    }

    public UploadStatus upload(ChunkDTO chunkDto) {
        String uploadToken = chunkDto.getUploadToken();
        if (!PRE_CHECK_MAP.containsKey(uploadToken)) {
            return UploadStatus.NOT_FOUND_TOKEN;
        }

        Long userId = SecurityUtils.getUserId();

        // 已存在
        if(chunkDto.getExist() == 1){
            Map<String, Object> paramMap = PRE_CHECK_MAP.get(uploadToken);

            // 数据不对
            if(!paramMap.get("exist").equals(1)){
                return UploadStatus.FILE_NOT_EXIST;
            }

            String md5 = (String) paramMap.get("md5");

            try {
                // 文件入库,并增加md5文件引用
                fileService.saveFileAndAddMd5Ref(userId, md5, chunkDto.getName());
            } catch (Exception e) {
                log.error("已存在文件入库失败 ==> userId: {}, uploadToken: {}, md5: {}", userId, uploadToken, md5);
                e.printStackTrace();
                return UploadStatus.ERROR_FILE_CANNOT_IN_DB;
            }

            return UploadStatus.CHUNK_SUCCESS;
        }

        // todo 文件上传
        UploadStatus resCode = this.fileUploadHandler.upload(chunkDto);

        // 文件重复性校验
        checkAndSave(resCode, chunkDto);

        return resCode;
    }

    public String download(File file, HttpServletRequest request, HttpServletResponse response) {
        // 用户存储的目录
        String path = storagePath + SecurityUtils.getUserId() + "/";

        // 查询数据库获取文件的存储名字
        File f = fileDao.selectByUserIdAndFileId(file.getUserId(), file.getFileId());
        if (f == null) {
            return null;
        }
        String[] split = f.getName().split("\\.");
        String s = path + f.getMd5() + "." + split[split.length - 1];
        java.io.File file1 = new java.io.File(s);
        fileDownloadHandle.download(file1, f.getName(), request, response);
        log.info(f.getName() + "下载完成！");
        return null;
    }

    protected void checkAndSave(UploadStatus resCode, ChunkDTO chunkDto) {
        UserBO loginUser = SecurityUtils.getLoginUser();
        if (loginUser == null) {
            throw new UnLoginException("无法获取登录用户信息");
        }

        Long userId = loginUser.getId();
        String fileName = chunkDto.getName();
        String userName = loginUser.getUsername();

        // 文件重复性校验
        if (resCode == UploadStatus.CHUNK_SUCCESS
                || (resCode == UploadStatus.SUCCESS_END
                && chunkDto.getChunk() != null
                && chunkDto.getChunks() != null)
        ) { // 只对上传成功的分片进行重复性校验
            Chunk chunk1 = chunkDao.selectByMd5(chunkDto.getMd5());
            if (chunk1 == null) { // 没有重复，则持久化存储
                log.info(chunkDto.getRealPath() + "分片无重复！");
                Chunk chunk = new Chunk(chunkDto.getMd5());
                chunkBo.save(chunk);
                chunkDto.setChunkId(chunk.getChunkId());
                // 设置标志不存在
                chunkDto.setExist(0);
            } else { // 否则删除刚刚上传的分片文件
                log.info(chunkDto.getRealPath() + "分片重复了！");
                // 存入查询到的id
                chunkDto.setChunkId(chunk1.getChunkId());

                //设置标志存在
                chunkDto.setExist(1);
            }

        }
        // 只对上传成功的完整（或合并的）文件校验
        if (resCode == UploadStatus.SUCCESS_WHOLE || resCode == UploadStatus.SUCCESS_END) {
            File file = null;
            synchronized (wholeFiles.getClass()) {
                file = wholeFiles.get(userId).get(fileName);
            }
            String md5 = file.getMd5();
            File selectByMd5File = fileDao.selectByMd5File(md5);
            if (selectByMd5File == null) { // 没有重复，则持久化存储
                log.info(file.getName() + "文件无重复！");
                fileBo.save(file);

                // 重命名完整文件
                ChunkDTO dto = new ChunkDTO();
                dto.setRealPath(storagePath + userId + "/" + file.getName());
                dto.setMd5(file.getMd5());
                dto.setChunkId(file.getFileId());
                md5Rename(storagePath + userId + "/", dto, true);

                // 存入关系表
                if (chunkDto.getChunk() != null
                        && chunkDto.getChunks() != null
                        && chunkDto.getChunk() == chunkDto.getChunks() - 1) {
                    Long fileId = wholeFiles.get(userId).get(fileName).getFileId();
                    List<FileChunk> fileChunks = uploaded.get(userId)
                            .get(fileName)
                            .stream()
                            .map(v -> {
                                while (v.getChunkId() == null) {
                                    try {
                                        Thread.sleep(100);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                                // 重命名分片
                                md5Rename(storagePath + "chunks/", v, false);
                                return new FileChunk(fileId, v.getChunkId(), v.getChunk());
                            })
                            .collect(Collectors.toList());
                    fileChunkDao.insertAllBatch(fileChunks);
                    log.info(fileName + "持久化成功！");
                    // 移除本次上传的缓存
                    uploaded.get(userId).remove(fileName);
                    wholeFiles.get(userId).remove(fileName);
                }

            } else { // 重复的
                log.info(file.getName() + "文件重复了！");
//                // 存入查询到的id
//                wholeFiles.get(userId).get(fileName).setFileId(selectByMd5File.getFileId());

                // 集中处理已存在的分片
                if (resCode == UploadStatus.SUCCESS_END) { // 是需要合并的文件才需要删除分片
                    uploaded.get(userId)
                            .get(fileName)
                            .stream().forEach(cd -> {
                                while (cd.getExist() == null) {
                                    try {
                                        Thread.sleep(100);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                                if (cd.getExist() == 1) {
                                    java.io.File f = new java.io.File(cd.getRealPath());
                                    f.delete();
                                }
                            });
                    // 移除本次上传的缓存
                    uploaded.get(userId).remove(fileName);
                }
                // 删除合并文件或完整文件
                String path = storagePath + userId + "/" + fileName;
                java.io.File f = new java.io.File(path);
                f.delete();

                // 移除本次上传的缓存
                wholeFiles.get(userId).remove(fileName);
            }

        }
    }

    private boolean md5Rename(String path, ChunkDTO chunkDto, boolean isAddSuffix) {
        java.io.File oldName = new java.io.File(chunkDto.getRealPath());
        String s;
        if (isAddSuffix) {
            s = path + chunkDto.getMd5() + "." + chunkDto.getRealPath().split("\\.")[1];
        } else {
            s = path + chunkDto.getMd5();
        }
        java.io.File newName = new java.io.File(s);

        if (!newName.exists()) {
            if (oldName.renameTo(newName)) {
                log.info("Rename Successful!");
                return true;
            } else {
                log.error("Rename Fail");
                return false;
            }
        }
        return false;
    }

    public void setFileUploadHandle(UploadHandleType type) {
        this.fileUploadHandler = FileUploadHandleFactory.getFileUploadHandle(type);
    }
}
