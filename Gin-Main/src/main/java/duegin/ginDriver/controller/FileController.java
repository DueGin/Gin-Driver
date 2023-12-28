package duegin.ginDriver.controller;

import com.ginDriver.core.domain.vo.ResultVO;
import duegin.ginDriver.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    public ResultVO<Void> upload(MultipartFile file) {
        return fileService.upload(FileService.FileType.system, file);
    }

    @PostMapping("upload/media")
    public ResultVO<Void> uploadMedia(MultipartFile file) {
        return fileService.upload(FileService.FileType.media, file);
    }

    @PostMapping("upload/movie")
    public ResultVO<Void> uploadMovie(MultipartFile file) {
        return fileService.upload(FileService.FileType.movie, file);
    }
}
