package com.ginDriver.main.controller;

import com.ginDriver.core.domain.vo.ResultVO;
import com.ginDriver.core.result.BusinessController;
import com.ginDriver.main.constant.FileType;
import com.ginDriver.main.domain.dto.file.FileDTO;
import com.ginDriver.main.domain.dto.file.PreUploadDTO;
import com.ginDriver.main.domain.vo.FileVO;
import com.ginDriver.main.file.FileManager;
import com.ginDriver.main.file.domain.dto.PreUploadRespDTO;
import com.ginDriver.main.file.download.MediaDownloadHandler;
import com.ginDriver.main.service.MinioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author DueGin
 */
@Slf4j
@BusinessController
@RequestMapping("file")
public class FileController {

    @Resource
    private MinioService minioService;

    @Resource
    private FileManager fileManager;

    @Resource
    private MediaDownloadHandler mediaDownloadHandler;

    @Value("${gin-driver.file.prefixPath}")
    private String prefixPath;

    @PostMapping("preUpload")
    public PreUploadRespDTO preUpload(@RequestBody PreUploadDTO dto) {
        return fileManager.preFileCheck(dto.getMd5(), dto.getContentType());
    }

    @PostMapping("upload/system")
    public ResultVO<FileVO> upload(MultipartFile file) {
        return minioService.upload(FileType.system, file);
    }

//    @PostMapping("upload/media")
//    public ResultVO<FileVO> uploadMedia(MultipartFile file) {
//        return fileService.upload(FileType.media, file);
//    }

    @PostMapping("upload/movie")
    public ResultVO<FileVO> uploadMovie(MultipartFile file) {
        return minioService.upload(FileType.movie, file);
    }

    @PostMapping("url")
    public ResultVO<FileVO> getUrlByFileName(@RequestBody FileDTO dto) {
        String objUrl = minioService.getObjUrl(dto.getBucketName(), dto.getFileName());
        return ResultVO.ok(new FileVO()
                .setFileName(dto.getFileName())
                .setUrl(objUrl)
        );
    }

    @DeleteMapping("delete")
    public ResultVO<Void> delete(@RequestBody FileDTO dto) {
        Boolean deleted = minioService.deleteFile(dto.getBucketName(), dto.getFileName());
        return deleted ? ResultVO.ok("删除成功！") : ResultVO.fail("删除失败");
    }

    @GetMapping("download")
    public void download(@RequestParam("fileId") Long fileId,
                         HttpServletRequest request,
                         HttpServletResponse response) {
        fileManager.download(fileId, request, response);
    }

    @GetMapping("download/media")
    public void downloadMedia(@RequestParam("fileId") Long fileId,
                              HttpServletRequest request,
                              HttpServletResponse response) {
        mediaDownloadHandler.download(prefixPath + "/", fileId, request, response);
    }
}
