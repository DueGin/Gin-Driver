package com.ginDriver.main.file;


import com.ginDriver.core.exception.ApiException;
import com.ginDriver.main.constant.FileType;
import com.ginDriver.main.domain.po.Dustbin;
import com.ginDriver.main.domain.po.File;
import com.ginDriver.main.domain.po.Md5File;
import com.ginDriver.main.file.constants.DownloadStatus;
import com.ginDriver.main.file.constants.UploadHandlerType;
import com.ginDriver.main.file.constants.UploadStatus;
import com.ginDriver.main.file.domain.dto.ChunkDTO;
import com.ginDriver.main.file.domain.dto.PreUploadRespDTO;
import com.ginDriver.main.file.domain.dto.UploadStatusDTO;
import com.ginDriver.main.file.download.FileDownloadHandler;
import com.ginDriver.main.file.download.IFileDownloadHandler;
import com.ginDriver.main.file.upload.FileUploadHandleFactory;
import com.ginDriver.main.file.upload.FileUploadHandler;
import com.ginDriver.main.security.utils.SecurityUtils;
import com.ginDriver.main.service.DustbinService;
import com.ginDriver.main.service.FileService;
import com.ginDriver.main.service.Md5FileService;
import com.ginDriver.main.service.MinioService;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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

    @Resource
    private MinioService minioService;
    /**
     * 已上传的分片文件集合(userId, (uploadId, [..分片DTO]))
     */
//    public final static Map<Long, Map<String, List<ChunkDTO>>> uploaded = new ConcurrentHashMap<>();
    public final static Map<String, List<Integer>> uploaded = new ConcurrentHashMap<>();

    /**
     * 已合并的文件集合(uploadId, {filePath, md5})
     */
    public final static Map<String, String> wholeFiles = new ConcurrentHashMap<>();

    /**
     * (upload_id, {upload_id, md5, exist})
     */
    public final static Map<String, PreUploadRespDTO> PRE_CHECK_MAP = new ConcurrentHashMap<>();

    private final static String src = "";

    @PostConstruct
    public void init() {
        storagePath = environment.getProperty("file.upload.location.storagePath");

    }

    public FileManager() {
        this.fileUploadHandler = FileUploadHandleFactory.getFileUploadHandle(UploadHandlerType.LOCATION);
        this.fileDownloadHandle = new FileDownloadHandler();
    }

    @Transactional
    public File saveFile(Long userId, String md5, String src, String contentType, String fileName, String objectName) {
        Md5File md5File = new Md5File();
        md5File.setMd5(md5);
        md5File.setObjectName(objectName);
        md5File.setSrc(src);
        md5File.setContentType(contentType);
        md5FileService.getBaseMapper().saveOrUpdate(md5File);
        Md5File md5File1 = md5FileService.lambdaQuery()
                .eq(Md5File::getMd5, md5)
                .eq(Md5File::getContentType, contentType)
                .one();

        File file = new File();
        file.setMd5FileId(md5File1.getId());
        file.setName(fileName);
        file.setUserId(userId);
        fileService.save(file);
        return file;
    }

    @Transactional
    public void removeFileLogic(Long fileId) {
        Long userId = SecurityUtils.getUserId();

        // 判断文件所有权是否为删除者
        File file = fileService.getById(fileId);

        // 文件不存在
        if (file == null) {
            return;
        }

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

        log.info("文件软删除成功 ==> fileId: {}, fileName: {}, md5FileId: {}, userId: {}", fileId, file.getName(), file.getMd5FileId(), userId);
    }

//    @Transactional
//    public boolean removeFile(Long fileId) {
//        // 判断是否已经软删除了
//        File file = fileService.getBaseMapper().getById(fileId);
//        if (file.getDeleted() != 1) {
//            return false;
//        }
//
//        // 删除垃圾桶
//        dustbinService.removeById(fileId);
//
//        // 删除文件
//        fileService.getBaseMapper().removeById(fileId);
//
//        // 更新md5文件引用次数
//        return md5FileService.lambdaUpdate().setSql("ref = ref - 1").eq(Md5File::getMd5, file.getMd5()).update();
//    }

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


    public PreUploadRespDTO preFileCheck(String md5, String contentType) {
        Md5File md5File = md5FileService.lambdaQuery()
                .eq(Md5File::getMd5, md5)
                .eq(Md5File::getContentType, contentType)
                .one();
        PreUploadRespDTO dto = new PreUploadRespDTO();
        dto.setMd5(md5);
        dto.setContentType(contentType);
        String uploadId = UUID.randomUUID().toString();
        dto.setUploadId(uploadId);
        dto.setExist(md5File != null ? 1 : 0);

        if (md5File != null && md5File.getRef() > 0) {
            File file = fileService.lambdaQuery().eq(File::getMd5FileId, md5File.getId()).last("limit 1").one();
            if (file != null) {
                dto.setObjectName(md5File.getObjectName());
                dto.setSrc(md5File.getSrc());
                dto.setFileType(file.getType());
            } else {
                md5FileService.removeByMd5AndContentType(md5, contentType);
                dto.setExist(0);
            }
        } else {
            md5FileService.removeByMd5AndContentType(md5, contentType);
            dto.setExist(0);
        }
        PRE_CHECK_MAP.put(uploadId, dto);
        return dto;
    }

    public UploadStatusDTO uploadAndSaveInMinio(ChunkDTO chunkDto, FileType fileType) {
        return upload(chunkDto, true, fileType);
    }

    public UploadStatusDTO upload(ChunkDTO chunkDto) {
        return upload(chunkDto, false, null);
    }

    private UploadStatusDTO upload(ChunkDTO chunkDto, boolean isSaveInObjectDB, FileType fileType) {
        if (chunkDto.getUploadId() == null) {
            throw new ApiException("请先申请uploadId");
        }

        String uploadId = chunkDto.getUploadId();
        if (StringUtils.isBlank(uploadId) || !PRE_CHECK_MAP.containsKey(uploadId)) {
            return new UploadStatusDTO(UploadStatus.NOT_FOUND_TOKEN);
        }

        Long userId = SecurityUtils.getUserId();
        File savedfile;

        FileManager self = (FileManager) AopContext.currentProxy();

        // 已存在
        if (chunkDto.getExist() == 1) {
            PreUploadRespDTO dto = PRE_CHECK_MAP.get(uploadId);

            // 数据不对
            if (dto.getExist() == null || dto.getExist() != 1) {
                return new UploadStatusDTO(UploadStatus.FILE_NOT_EXIST);
            }

            String md5 = dto.getMd5();

            try {
                // 文件入库,并增加md5文件引用
                savedfile = self.saveFile(userId, md5, dto.getSrc(), chunkDto.getContentType(), chunkDto.getName(), null);
            } catch (Exception e) {
                log.error("已存在文件入库失败 ==> userId: {}, uploadId: {}, md5: {}", userId, uploadId, md5);
                e.printStackTrace();
                return new UploadStatusDTO(UploadStatus.ERROR_FILE_CANNOT_IN_DB);
            }

            String objectName = null, src = null;

            if (isSaveInObjectDB) {
                objectName = dto.getObjectName();
                src = dto.getSrc();
            }

            return new UploadStatusDTO(UploadStatus.SUCCESS_END, objectName, savedfile, src, md5);
        }

        // 上传文件
        UploadStatusDTO uploadDTO = this.fileUploadHandler.upload(chunkDto);

        // 传完了，合并完了
        if (uploadDTO.getUploadStatus() == UploadStatus.SUCCESS_END) {

            // 存入minio
            String objectName = "";
            if (isSaveInObjectDB) {
                String filePath = uploadDTO.getFilePath();
                objectName = saveInMinio(filePath, fileType, chunkDto.getContentType());
                uploadDTO.setObjectName(objectName);
            }

            // 入库
            savedfile = self.saveFile(userId, uploadDTO.getMd5(), uploadDTO.getFilePath(), chunkDto.getContentType(), chunkDto.getName(), objectName);
            uploadDTO.setFile(savedfile);

            // 删除uploaded
            uploaded.remove(chunkDto.getMd5());

            return uploadDTO;
        } else {
            // todo 判断分片上传状态，再做其他操作，例如，记录已上传分片序号
            return uploadDTO;
        }

    }

    /**
     * 保存至minio todo 在上传时作压缩图片
     *
     * @param filePath 文件路径
     * @param fileType {@link FileType} 文件类型(minio桶名称)
     * @return minio对象名称
     */
    private String saveInMinio(String filePath, FileType fileType, String contentType) {
        java.io.File file = new java.io.File(filePath);

        // 存入minio
        try (FileInputStream fis = new FileInputStream(file)) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            byte[] bytes;
            // 压缩图片 todo 后面抽离出来，做成oss策略
            if (!filePath.endsWith(".mp4") && !filePath.endsWith(".mov")) {
                Thumbnails.of(fis)
                        .scale(1) // 缩放比例
                        .outputQuality(0.5) // 质量
                        .toOutputStream(bos);
                bytes = bos.toByteArray();
            } else {
                bytes = fis.readAllBytes();
            }

            ByteArrayInputStream is = new ByteArrayInputStream(bytes);

            return minioService.uploadWithType(fileType, file.getName(), contentType, is);
        } catch (IOException e) {
            log.error("保存至minio失败 ==> " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 下载文件
     */
    public DownloadStatus download(Long fileId, HttpServletRequest request, HttpServletResponse response) {
        // 用户存储的目录
        String path = storagePath + SecurityUtils.getUserId() + "/";

        File f = fileService.getById(fileId);
        if (f == null) {
            return DownloadStatus.FILE_NOT_FOUND;
        }
        Md5File md5File = md5FileService.getById(f.getMd5FileId());


        // 找出物理文件
        int lastedIndexOf = f.getName().lastIndexOf('.');
        String s = path + md5File.getMd5() + f.getName().substring(lastedIndexOf);
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
