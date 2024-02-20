package com.ginDriver.main.service;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ginDriver.core.domain.po.User;
import com.ginDriver.core.exception.ApiException;
import com.ginDriver.core.service.impl.MyServiceImpl;
import com.ginDriver.main.domain.dto.exif.ExifInfoDTO;
import com.ginDriver.main.domain.dto.media.MediaPageDTO;
import com.ginDriver.main.domain.po.Dustbin;
import com.ginDriver.main.domain.po.Media;
import com.ginDriver.main.domain.po.MediaExif;
import com.ginDriver.main.domain.vo.FileVO;
import com.ginDriver.main.domain.vo.MediaVO;
import com.ginDriver.main.mapper.MediaMapper;
import com.ginDriver.main.security.utils.SecurityUtils;
import com.ginDriver.main.utils.GeoUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

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

    @Resource
    private Executor serviceExecutor;

    private static final Integer EXPIRE = 24 * 60 * 60;

    // todo 解析压缩包图片，做批量上传解析

    /**
     * 存入minio，存入db
     *
     * @param file        文件
     * @param exifInfoDTO exif信息
     */
    @Transactional
    public FileVO save(MultipartFile file, ExifInfoDTO exifInfoDTO) {
        // 解析出exif信息
        // 存储至物理硬盘
        // 将缩略图存入minio
        Long userId = SecurityUtils.getUserId();


        // 存入minio
        String objName = fileService.uploadWithType(media, file);
        // 获取url
        String objUrl = fileService.getObjUrl(media, objName);

        // todo 加入队列异步获取exif信息

        Media m = new Media();
        m.setUserId(userId);
        m.setFileName(objName);
        m.setMimeType(file.getContentType());

        MediaExif exif = new MediaExif();
        try {
            // 存入db
            super.save(m);

            // 保存exif
            BeanUtils.copyProperties(exifInfoDTO, exif);
            exif.setMediaId(m.getId());
            mediaExifService.save(exif);

        } catch (Exception e) {
            log.error(e.getMessage());
            // 删除minio
            fileService.deleteFile(FileService.FileType.media, m.getFileName());
            throw new ApiException(e.getMessage());
        }
        String longitude = exifInfoDTO.getLongitude();
        String latitude = exifInfoDTO.getLatitude();
        if (StringUtils.isNotBlank(longitude) && StringUtils.isNotBlank(latitude)) {
            serviceExecutor.execute(() -> {
                log.info("executor handle ==> longitude: {}, latitude: {}", longitude, latitude);
                JSONObject jsonObject = GeoUtil.getAddressWithLngLat(longitude, latitude);
                // 解析数据，更新exif数据库
                if ("10000".equals(jsonObject.get("infocode"))) {
                    JSONObject regeocode = (JSONObject) jsonObject.get("regeocode");
                    JSONObject addressComponent = (JSONObject) regeocode.get("addressComponent");
                    // 行政代码
                    String adcode = (String) addressComponent.get("adcode");
                    log.info("adcode: {}", adcode);
                    if (exif.getId() != null) {
                        exif.setAdcode(Integer.valueOf(adcode));
                        mediaExifService.updateById(exif);
                    }
                } else {
                    log.warn("无法获取行政区编码=>> lat: {}, lng: {}, userId: {}, mediaId: {}, exifId: {}", latitude, longitude, userId, m.getId(), exif.getId());
                }
            });
        }

        return new FileVO()
                .setFileName(objName)
                .setUrl(objUrl);
    }

    /**
     * 获取媒体VO分页
     *
     * @param mediaPageDTO 分页查询对象
     * @return 带url的媒体VO分页
     */
    public Page<MediaVO> getMediaPage(MediaPageDTO mediaPageDTO) {
        Long userId = SecurityUtils.getUserId();

        LambdaQueryWrapper<Media> qw = new QueryWrapper<Media>().lambda()
                .eq(Media::getUserId, userId);
//                .or()
//                .eq(mediaPageDTO.getOnlyLookSelf() != null && mediaPageDTO.getOnlyLookSelf(), Media::getSelf, 0);

        super.page(mediaPageDTO, qw);

        if (mediaPageDTO.getRecords().isEmpty()) {
            Page<MediaVO> voPage = new Page<>();
            BeanUtils.copyProperties(mediaPageDTO, voPage);
            return voPage;
        }

        // 转换数据，并设置minio地址，用户信息
        List<MediaVO> vos = convertToVoListWithUsername(mediaPageDTO.getRecords());

        Page<MediaVO> voPage = new Page<>();
        BeanUtils.copyProperties(mediaPageDTO, voPage, "records");
        voPage.setRecords(vos);
        return voPage;
    }

    /**
     * 转换MediaVO，并设置minio地址，媒体所属用户信息
     *
     * @param list media集合
     * @return {@link List<MediaVO>} VO集合
     */
    public List<MediaVO> convertToVoListWithUsername(List<Media> list) {
        // 获取用户信息
        Set<Long> userIdSet = list.stream().map(Media::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userMap = userService.list(new QueryWrapper<User>().lambda()
                .in(!CollectionUtils.isEmpty(userIdSet), User::getId, userIdSet)
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
     * @param list mediaVO集合
     */
    public void setMinioUrl(List<MediaVO> list) {
        list.forEach(vo -> {
            // 设置minio url
            String objUrl = fileService.getObjUrl(media, vo.getFileName(), EXPIRE);
            vo.setUrl(objUrl);
        });
    }

    public void delete(Long[] ids) {
        Long userId = SecurityUtils.getUserId();
        // 只有自己上传的才能删
        for (Long id : ids) {
            Media m = super.getById(id);
            if (!m.getUserId().equals(userId)) {
                throw new ApiException("删除失败，不能删除别人的照片");
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
    }
}
