package com.ginDriver.main.service;

import com.ginDriver.core.domain.po.User;
import com.ginDriver.core.domain.vo.ResultVO;
import com.ginDriver.core.service.impl.MyServiceImpl;
import com.ginDriver.main.domain.dto.media.MediaDTO;
import com.ginDriver.main.domain.po.Dustbin;
import com.ginDriver.main.domain.po.Media;
import com.ginDriver.main.domain.po.MediaExif;
import com.ginDriver.main.domain.vo.FileVO;
import com.ginDriver.main.domain.vo.MediaVO;
import com.ginDriver.main.mapper.MediaMapper;
import com.ginDriver.main.security.utils.SecurityUtils;
import com.ginDriver.main.utils.GpsExifInfoUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.ginDriver.main.domain.po.table.MediaTableDef.MEDIA;
import static com.ginDriver.main.service.FileService.FileType.media;

/**
 * @author DueGin
 */
@Slf4j
@Service
public class MediaService extends MyServiceImpl<MediaMapper, Media> {

    @Resource
    private FileService fileService;
    @Resource
    private UserService userService;

    @Resource
    private DustbinService dustbinService;

    @Resource
    private MediaExifService mediaExifService;

    private static final Integer EXPIRE = 24 * 60 * 60;


    /**
     * 存入minio，存入db
     *
     * @param files 文件
     */
    @Deprecated
    public void saveBatch(MultipartFile[] files) {
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
            media.setMimeType(file.getContentType());
            mediaList.add(media);
        }

        // 入库
        List<String> unSaveList = new ArrayList<>();
        for (Media m : mediaList) {
            try {
                super.save(m);
            } catch (Exception e) {
                log.error(e.getMessage());
                unSaveList.add(m.getFileName());
            }
        }

        // 删除minio
        for (String name : unSaveList) {
            fileService.deleteFile(media, name);
        }
    }

    /**
     * 存入minio，存入db
     *
     * @param file 文件
     */
    public FileVO save(MultipartFile file) {
        // 解析出exif信息
        // 存储至物理硬盘
        // 将缩略图存入minio


        // 存入minio
        String objName = fileService.uploadWithType(media, file);
        // 获取url
        String objUrl = fileService.getObjUrl(media, objName);

        // todo 加入队列异步获取exif信息

        Media m = new Media();
        m.setUserId(SecurityUtils.getUserId());
        m.setFileName(objName);
        m.setMimeType(file.getContentType());

        try {
            Map<String, String> exifInfo = GpsExifInfoUtil.readExifInfo(file.getInputStream());
            MediaExif exif = GpsExifInfoUtil.convertToExifInfoDTO(exifInfo);
            m.setMediaDate(exif.getCreateTime() != null ? exif.getCreateTime().toLocalDate() : null);
            // 存入db
            super.save(m);

            exif.setMediaId(m.getId());
//             保存exif
            mediaExifService.save(exif);
        } catch (Exception e) {
            log.error(e.getMessage());
            // 删除minio
            fileService.deleteFile(FileService.FileType.media, m.getFileName());
            throw new RuntimeException(e);
        }

        return new FileVO()
                .setFileName(objName)
                .setUrl(objUrl);
    }

    /**
     * 获取媒体VO分页
     *
     * @param mediaDTO 分页查询对象
     * @return 带url的媒体VO分页
     */
    public Page<MediaVO> getMediaPage(MediaDTO mediaDTO) {
        Long userId = SecurityUtils.getUserId();
        Page<Media> page = super.page(mediaDTO, QueryWrapper.create()
                .from(Media.class)
                .eq(Media::getUserId, userId)
                .or(MEDIA.SELF.eq(0, mediaDTO.getOnlyLookSelf() != null && mediaDTO.getOnlyLookSelf()))
        );

        if (page.getRecords().isEmpty()) {
            Page<MediaVO> voPage = new Page<>();
            BeanUtils.copyProperties(mediaDTO, voPage);
            return voPage;
        }

        // 转换数据，并设置minio地址，用户信息
        List<MediaVO> vos = convertToVOListWithUsername(page.getRecords());

        Page<MediaVO> voPage = new Page<>();
        BeanUtils.copyProperties(mediaDTO, voPage, "records");
        voPage.setRecords(vos);
        return voPage;
    }

    /**
     * 转换MediaVO，并设置minio地址，媒体所属用户信息
     *
     * @param list    media集合
     * @return {@code List<MediaVO>} VO集合
     */
    public List<MediaVO> convertToVOListWithUsername(List<Media> list) {
        // 获取用户信息
        Set<Long> userIdSet = list.stream().map(Media::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userMap = userService.list(QueryWrapper.create()
                .from(User.class)
                .in(User::getId, userIdSet)
        ).stream().collect(Collectors.groupingBy(User::getId));

        return list.stream().map(m -> {
            MediaVO vo = new MediaVO();
            BeanUtils.copyProperties(m, vo);
            List<User> users = userMap.get(vo.getUserId());
            vo.setUsername(users.get(0).getUsername());
            // 设置minio url
            String objUrl = fileService.getObjUrl(media, vo.getFileName(), EXPIRE);
            vo.setUrl(objUrl);
            return vo;
        }).collect(Collectors.toList());
    }

    /**
     * 设置minio地址
     *
     * @param list    mediaVO集合
     * @return {@code List<MediaVO>} VO集合
     */
    public void setMinioUrl(List<MediaVO> list) {
        list.forEach(vo -> {
            // 设置minio url
            String objUrl = fileService.getObjUrl(media, vo.getFileName(), EXPIRE);
            vo.setUrl(objUrl);
        });
    }

    public ResultVO<Void> delete(Long[] ids) {
        Long userId = SecurityUtils.getUserId();
        // 只有自己上传的才能删
        for (Long id : ids) {
            Media m = super.getById(id);
            if (!m.getUserId().equals(userId)) {
                return ResultVO.fail("删除失败，不能删除别人的媒体");
            }

            // 存入垃圾箱
            Dustbin dustbin = new Dustbin();
            dustbin.setType(media.value);
            dustbin.setFileName(m.getFileName());
            dustbin.setUserId(userId);
            dustbin.setMediaId(m.getId());
            dustbinService.save(dustbin);

            // 软删除媒体表
            super.removeById(id);
        }
        return ResultVO.ok("删除成功！");
    }
}
