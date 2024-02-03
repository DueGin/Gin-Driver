package com.ginDriver.main.controller;

import com.ginDriver.core.domain.vo.ResultVO;
import com.ginDriver.main.domain.dto.file.FileDTO;
import com.ginDriver.main.domain.vo.FileVO;
import com.ginDriver.main.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @author DueGin
 */
@Slf4j
@RestController
@RequestMapping("file")
public class FileController {

    @Resource
    private FileService fileService;

    @PostMapping("upload/system")
    public ResultVO<FileVO> upload(MultipartFile file) {
        return fileService.upload(FileService.FileType.system, file);
    }

    @PostMapping("upload/media")
    public ResultVO<FileVO> uploadMedia(MultipartFile file) {
        return fileService.upload(FileService.FileType.media, file);
    }

    @PostMapping("upload/movie")
    public ResultVO<FileVO> uploadMovie(MultipartFile file) {
        return fileService.upload(FileService.FileType.movie, file);
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
