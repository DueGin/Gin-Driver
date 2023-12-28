package duegin.ginDriver.service;

import com.ginDriver.core.service.impl.MyServiceImpl;
import duegin.ginDriver.domain.po.Media;
import duegin.ginDriver.mapper.MediaMapper;
import duegin.ginDriver.security.utils.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static duegin.ginDriver.service.FileService.FileType.media;

/**
 * @author DueGin
 */
@Service
public class MediaService extends MyServiceImpl<MediaMapper, Media> {

    @Resource
    private FileService fileService;

    /**
     * 存入minio，存入db
     *
     * @param files 文件
     */
    public void save(MultipartFile[] files) {
        // 解析出exif信息
        // 存储至物理硬盘
        // 将缩略图存入minio


        List<Media> mediaList = new ArrayList<>();
        for (MultipartFile file : files) {
            // 存入minio
            String objName = fileService.uploadWithType(media, file);
            // 获取url
            String objUrl = fileService.getObjUrl(media, objName);

            // todo 加入队列异步获取exif信息

            // 存入db
            Media media = new Media();
            media.setUserId(SecurityUtils.getUserId());
            media.setFileName(objName);
            media.setType(FileService.FileType.media.value);
            media.setFormat(file.getContentType());
            media.setUrl(objUrl);
            mediaList.add(media);
        }
        super.saveBatch(mediaList);
    }


}
