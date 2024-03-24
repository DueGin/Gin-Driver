package com.ginDriver.main.file;


import com.ginDriver.main.domain.po.Dustbin;
import com.ginDriver.main.domain.po.File;
import com.ginDriver.main.domain.po.Md5File;
import com.ginDriver.main.file.constants.DownloadStatus;
import com.ginDriver.main.file.constants.UploadHandlerType;
import com.ginDriver.main.file.constants.UploadStatus;
import com.ginDriver.main.file.domain.dto.ChunkDTO;
import com.ginDriver.main.file.domain.dto.UploadStatusDTO;
import com.ginDriver.main.file.download.FileDownloadHandler;
import com.ginDriver.main.file.download.IFileDownloadHandler;
import com.ginDriver.main.file.upload.FileUploadHandleFactory;
import com.ginDriver.main.file.upload.FileUploadHandler;
import com.ginDriver.main.security.utils.SecurityUtils;
import com.ginDriver.main.service.DustbinService;
import com.ginDriver.main.service.FileService;
import com.ginDriver.main.service.Md5FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author DueGin
 */
@Service
@Slf4j
public class FileManager {

    private FileUploadHandler fileUploadHandler;

    private IFileDownloadHandler fileDownloadHandle;

    public static String storagePath;

    @Resource
    private Environment environment;

    @Resource
    private Md5FileService md5FileService;

    @Resource
    private FileService fileService;

    @Resource
    private DustbinService dustbinService;

    /**
     * 已上传的分片文件集合(userId, (fileName, [..分片DTO]))
     */
    public final static Map<Long, Map<String, List<ChunkDTO>>> uploaded = new ConcurrentHashMap<>();

    /**
     * 已合并的文件集合(uploadId, {filePath, md5})
     */
    public final static Map<String, String> wholeFiles = new ConcurrentHashMap<>();

    /**
     * (upload_id, {upload_id, md5, exist})
     */
    public final static Map<String, Map<String, Object>> PRE_CHECK_MAP = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        storagePath = environment.getProperty("file.upload.location.storagePath");
    }

    public FileManager() {
        this.fileUploadHandler = FileUploadHandleFactory.getFileUploadHandle(UploadHandlerType.LOCATION);
        this.fileDownloadHandle = new FileDownloadHandler();
    }

    @Transactional
    public Long saveFile(Long userId, String md5, String fileName) {
        md5FileService.lambdaUpdate().setSql("ref = ref + 1").eq(Md5File::getMd5, md5).update();
        File file = new File();
        file.setMd5(md5);
        file.setName(fileName);
        file.setUserId(userId);
        fileService.save(file);
        return file.getId();
    }

    @Transactional
    public void removeFileLogic(Long fileId) {
        Long userId = SecurityUtils.getUserId();

        // 判断文件所有权是否为删除者
        File file = fileService.getById(fileId);

        // 删的是别人的文件
        if (!file.getUserId().equals(userId)) {
            // todo 暂时先不做处理
            return;
        }

        // 软删除文件
        fileService.removeById(fileId);

        // 存入垃圾箱
        Dustbin dustbin = new Dustbin();
        dustbin.setType(file.getType());
        dustbin.setFileName(file.getName());
        dustbin.setUserId(userId);
        dustbin.setFileId(fileId);
        dustbinService.save(dustbin);

        log.info("文件软删除成功 ==> fileId: {}, fileName: {}, md5: {}, userId: {}", fileId, file.getName(), file.getMd5(), userId);
    }

    @Transactional
    public boolean removeFile(Long fileId) {
        // 判断是否已经软删除了
        File file = fileService.getBaseMapper().getById(fileId);
        if (file.getDeleted() != 1) {
            return false;
        }

        // 删除垃圾桶
        dustbinService.removeById(fileId);

        // 删除文件
        fileService.getBaseMapper().removeById(fileId);

        // 更新md5文件引用次数
        return md5FileService.lambdaUpdate().setSql("ref = ref - 1").eq(Md5File::getMd5, file.getMd5()).update();
    }

    @Transactional
    public void rebornFileBatch(Collection<Long> dustbinIds) {
        if (dustbinIds == null || dustbinIds.isEmpty()) {
            return;
        }

        List<Long> fileIds = dustbinService.lambdaQuery()
                .in(Dustbin::getId, dustbinIds)
                .list()
                .stream()
                .map(Dustbin::getFileId)
                .collect(Collectors.toList());

        if (!CollectionUtils.isEmpty(fileIds)) {
            fileService.getBaseMapper().updateDeletedByIds(fileIds, 0);
            dustbinService.removeByIds(dustbinIds);
        }

    }


    public Map<String, Object> preFileCheck(String md5) {
        Md5File md5File = md5FileService.lambdaQuery().eq(Md5File::getMd5, md5).one();
        Map<String, Object> map = new HashMap<>();
        map.put("md5", md5);
        String uploadId = UUID.randomUUID().toString();
        map.put("upload_id", uploadId);
        map.put("exist", md5File != null ? 1 : 0);
        if (md5File != null) {
            File file = fileService.lambdaQuery().eq(File::getMd5, md5).last("limit 1").one();
            map.put("object_name", md5File.getObjectName());
            map.put("src", md5File.getSrc());
            map.put("type", file.getType());
        }
        PRE_CHECK_MAP.put(uploadId, map);
        return map;
    }

    public UploadStatusDTO uploadAndSaveInMinio(ChunkDTO chunkDto, FileService.FileType fileType) {
        return upload(chunkDto, true, fileType);
    }

    public UploadStatusDTO upload(ChunkDTO chunkDto) {
        return upload(chunkDto, false, null);
    }

    private UploadStatusDTO upload(ChunkDTO chunkDto, boolean isSaveInObjectDB, FileService.FileType fileType) {
        String uploadId = chunkDto.getUploadId();
        if (!PRE_CHECK_MAP.containsKey(uploadId)) {
            return new UploadStatusDTO(UploadStatus.NOT_FOUND_TOKEN);
        }

        Long userId = SecurityUtils.getUserId();
        Long fileId;

        // 已存在
        if (chunkDto.getExist() == 1) {
            Map<String, Object> paramMap = PRE_CHECK_MAP.get(uploadId);

            // 数据不对
            if (!paramMap.get("exist").equals(1)) {
                return new UploadStatusDTO(UploadStatus.FILE_NOT_EXIST);
            }

            String md5 = (String) paramMap.get("md5");

            try {
                // 文件入库,并增加md5文件引用 todo exp
                fileId = this.saveFile(userId, md5, chunkDto.getName());
            } catch (Exception e) {
                log.error("已存在文件入库失败 ==> userId: {}, uploadId: {}, md5: {}", userId, uploadId, md5);
                e.printStackTrace();
                return new UploadStatusDTO(UploadStatus.ERROR_FILE_CANNOT_IN_DB);
            }

            String objectName = null, src = null;

            if (isSaveInObjectDB) {
                objectName = (String) paramMap.get("object_name");
                src = (String) paramMap.get("src");
            }

            return new UploadStatusDTO(UploadStatus.SUCCESS_END, objectName, fileId, src, md5);
        }

        // 上传文件
        UploadStatusDTO uploadDTO = this.fileUploadHandler.upload(chunkDto);
        if (uploadDTO.getUploadStatus() == UploadStatus.SUCCESS_END) {

            // 入库 todo exp
            fileId = this.saveFile(userId, uploadDTO.getMd5(), chunkDto.getName());
            uploadDTO.setFileId(fileId);

            // 存入minio
            if (isSaveInObjectDB) {
                String filePath = uploadDTO.getFilePath();
                String objectName = saveInMinio(filePath, fileType);
                uploadDTO.setObjectName(objectName);
            }

            return uploadDTO;
        } else {
            // todo 判断分片上传状态，再做其他操作，例如，记录已上传分片序号
            return uploadDTO;
        }

    }

    /**
     * 保存至minio
     *
     * @param filePath 文件路径
     * @param fileType {@link com.ginDriver.main.service.FileService.FileType} 文件类型(minio桶名称)
     * @return minio对象名称
     */
    private String saveInMinio(String filePath, FileService.FileType fileType) {
        java.io.File file = new java.io.File(filePath);
        // 转换为 Path 对象
        Path path = Paths.get(file.toURI());
        String contentType = "";

        try {
            // 使用 Files.probeContentType 方法获取文件的内容类型
            contentType = Files.probeContentType(path);
        } catch (IOException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }

        // 存入minio
        try (FileInputStream fis = new FileInputStream(file)) {
            return fileService.uploadWithType(fileType, file.getName(), contentType, fis);
        } catch (IOException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 下载文件
     */
    public DownloadStatus download(String fileId, HttpServletRequest request, HttpServletResponse response) {
        // 用户存储的目录
        String path = storagePath + SecurityUtils.getUserId() + "/";

        File f = fileService.getById(fileId);

        if (f == null) {
            return DownloadStatus.FILE_NOT_FOUND;
        }

        // 找出物理文件
        String[] split = f.getName().split("\\.");
        String s = path + f.getMd5() + "." + split[split.length - 1];
        java.io.File file = new java.io.File(s);
        fileDownloadHandle.download(file, f.getName(), request, response);
        log.info(f.getName() + "下载完成！");
        return DownloadStatus.SUCCESS;
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

    public void setFileUploadHandle(UploadHandlerType type) {
        this.fileUploadHandler = FileUploadHandleFactory.getFileUploadHandle(type);
    }
}
