package com.ginDriver.main.controller;

import com.ginDriver.core.domain.vo.ResultVO;
import com.ginDriver.core.result.BusinessController;
import com.ginDriver.main.constant.FileType;
import com.ginDriver.main.domain.dto.file.FileDTO;
import com.ginDriver.main.domain.vo.FileVO;
import com.ginDriver.main.file.FileManager;
import com.ginDriver.main.file.domain.dto.PreUploadRespDTO;
import com.ginDriver.main.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @author DueGin
 */
@Slf4j
@BusinessController
@RequestMapping("file")
public class FileController {

    @Resource
    private FileService fileService;

    @Resource
    private FileManager fileManager;

    @PostMapping("preUpload")
    public PreUploadRespDTO preUpload(@RequestBody String md5) {
        return fileManager.preFileCheck(md5);
    }

    @PostMapping("upload/system")
    public ResultVO<FileVO> upload(MultipartFile file) {
        return fileService.upload(FileType.system, file);
    }

    @PostMapping("upload/media")
    public ResultVO<FileVO> uploadMedia(MultipartFile file) {
        return fileService.upload(FileType.media, file);
    }

    @PostMapping("upload/movie")
    public ResultVO<FileVO> uploadMovie(MultipartFile file) {
        return fileService.upload(FileType.movie, file);
    }

    @PostMapping("url")
    public ResultVO<FileVO> getUrlByFileName(@RequestBody FileDTO dto) {
        String objUrl = fileService.getObjUrl(dto.getBucketName(), dto.getFileName());
        return ResultVO.ok(new FileVO()
                .setFileName(dto.getFileName())
                .setUrl(objUrl)
        );
    }

    @DeleteMapping("delete")
    public ResultVO<Void> delete(@RequestBody FileDTO dto) {
        Boolean deleted = fileService.deleteFile(dto.getBucketName(), dto.getFileName());
        return deleted ? ResultVO.ok("删除成功！") : ResultVO.fail("删除失败");
    }
}
