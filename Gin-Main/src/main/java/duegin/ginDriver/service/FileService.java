package duegin.ginDriver.service;

import com.ginDriver.common.minio.service.MinioComponent;
import com.ginDriver.core.domain.vo.ResultVO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.UUID;

/**
 * @author DueGin
 */
@Slf4j
@Service
public class FileService {

    @Resource
    private MinioComponent minioComponent;

    public ResultVO<Void> upload(FileType fileType, MultipartFile file) {
        String objName = uploadWithType(fileType, file);
        String objectUrl = minioComponent.getObjectUrl(fileType.getClass().getName(), objName);
        return ResultVO.ok(objectUrl);
    }

    public String getObjUrl(FileType fileType, String objectName) {
        return minioComponent.getObjectUrl(fileType.getClass().getName(), objectName);
    }


    /**
     * 返回访问地址
     *
     * @param fileType 文件类型
     * @param file     文件
     * @return 对象名字
     */
    public String uploadWithType(FileType fileType, MultipartFile file) {
        String name = fileType.getClass().getName();
        String objName = file.getName() + UUID.randomUUID().toString().replaceAll("-", "");
        try {
            minioComponent.putObject(name, objName, file.getInputStream(), file.getContentType());
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        return objName;
    }

    @AllArgsConstructor
    public enum FileType {
        system(1),
        movie(2),
        media(3),
        other(4);
        public final Integer value;
    }
}
