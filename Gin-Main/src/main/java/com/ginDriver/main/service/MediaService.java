package com.ginDriver.main.service;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ginDriver.core.exception.ApiException;
import com.ginDriver.core.service.impl.MyServiceImpl;
import com.ginDriver.main.constant.FileType;
import com.ginDriver.main.domain.dto.media.MediaFileUploadDTO;
import com.ginDriver.main.domain.dto.media.MediaPageDTO;
import com.ginDriver.main.domain.po.File;
import com.ginDriver.main.domain.po.Media;
import com.ginDriver.main.domain.vo.FileVO;
import com.ginDriver.main.domain.vo.MediaVO;
import com.ginDriver.main.file.FileManager;
import com.ginDriver.main.file.constants.UploadStatus;
import com.ginDriver.main.file.domain.dto.UploadStatusDTO;
import com.ginDriver.main.mapper.MediaMapper;
import com.ginDriver.main.security.utils.SecurityUtils;
import com.ginDriver.main.utils.GeoUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;


/**
 * @author DueGin
 */
@Slf4j
@Service
public class MediaService extends MyServiceImpl<MediaMapper, Media> {

    @Resource
    private FileService fileService;

    @Resource
    private Executor serviceExecutor;

    @Resource
    private FileManager fileManager;

    private static final Integer EXPIRE = 24 * 60 * 60;

//    private static final Integer thumbnailSize = 1000000;

    private static final Map<String, Media> UPLOAD_INFO_MAP = new ConcurrentHashMap<>();

    /**
     * 存入minio，存入db
     *
     * @param mediaFileUploadDTO exif信息
     */
    @Transactional
    public FileVO save(MediaFileUploadDTO mediaFileUploadDTO) {
        // 解析出exif信息
        // 存储至物理硬盘
        // 将缩略图存入minio
        Long userId = SecurityUtils.getUserId();

        UploadStatusDTO uploadStatusDTO = fileManager.uploadAndSaveInMinio(mediaFileUploadDTO, FileType.media);

        String uploadId = mediaFileUploadDTO.getUploadId();
        if (mediaFileUploadDTO.getHasInfo() != null && mediaFileUploadDTO.getHasInfo() == 1) {
            // 保存exif
            Media m = new Media();
            BeanUtils.copyProperties(mediaFileUploadDTO, m);
            UPLOAD_INFO_MAP.put(uploadId, m);
        }

        // 没传完，只是上传完分片
        if (uploadStatusDTO.getUploadStatus() != UploadStatus.SUCCESS_END) {
            return null;
        }

        // 传完了，且合并完了

        Media m = UPLOAD_INFO_MAP.get(uploadId);
        if (m == null) {
            log.error("上传文件有误 ==> uploadId: {}, uploadStatusDTO: {}", uploadId, uploadStatusDTO);
            throw new ApiException("上传文件有误");
        }

        // 存入db
        m.setFileId(uploadStatusDTO.getFile().getId());
        m.setMd5FileId(uploadStatusDTO.getFile().getMd5FileId());
        super.save(m);

        // 更新file表的type
        if (m.getFileId() == null) {
            log.error("上传文件后返回文件ID为null ==> uploadId: {}, uploadStatusDTO: {}", uploadId, uploadStatusDTO);
        } else {
            fileService.lambdaUpdate().set(File::getType, FileType.media.ordinal()).eq(File::getId, m.getFileId()).update();
        }

        String longitude = mediaFileUploadDTO.getLongitude();
        String latitude = mediaFileUploadDTO.getLatitude();
        // 获取高德地图行政区编码
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
                    if (m.getId() != null) {
                        m.setAdcode(Integer.valueOf(adcode));
                        super.updateById(m);
                    }
                } else {
                    log.warn("无法获取行政区编码 ==> lat: {}, lng: {}, userId: {}, mediaId: {}, exifId: {}", latitude, longitude, userId, m.getId(), m.getId());
                }
            });
        }

        String objUrl = fileService.getObjUrl(FileType.media, uploadStatusDTO.getObjectName());

        return new FileVO()
                .setFileName(uploadStatusDTO.getObjectName())
                .setUrl(objUrl);
    }

    /**
     * 获取媒体VO分页 todo 获取minio缩略图
     *
     * @param mediaPageDTO 分页查询对象
     * @return 带url的媒体VO分页
     */
    public Page<MediaVO> getMediaPage(MediaPageDTO mediaPageDTO) {
        Long userId = SecurityUtils.getUserId();

        // 查询
        this.getBaseMapper().selectPageWithFile(mediaPageDTO, userId, mediaPageDTO.getOnlyLookSelf());

        if (mediaPageDTO.getRecords().isEmpty()) {
            Page<MediaVO> voPage = new Page<>();
            BeanUtils.copyProperties(mediaPageDTO, voPage);
            return voPage;
        }

        // 设置minio地址
        setObjectDbUrl(mediaPageDTO.getRecords());

        Page<MediaVO> voPage = new Page<>();
        BeanUtils.copyProperties(mediaPageDTO, voPage);
        return voPage;
    }

    /**
     * 设置minio地址
     *
     * @param list mediaVO集合
     */
    public void setObjectDbUrl(List<MediaVO> list) {
        list.forEach(vo -> {
            // 设置minio url
//            String objUrl = fileService.getObjUrl(FileType.media, vo.getObjectName(), EXPIRE);
            String objUrl = fileService.getFileBase64(FileType.media.name(), vo.getObjectName(), 0.7, 0.7, null, null);
            vo.setUrl(objUrl);
            vo.setObjectName(null);
        });
    }

    public List<MediaVO> getThatYearTodayList(Integer limit) {
        MediaMapper mediaMapper = super.getBaseMapper();
        Long userId = SecurityUtils.getUserId();
        if (userId == null) {
            throw new ApiException("你登录了吗？");
        }

        LocalDate today = LocalDate.now();
        List<MediaVO> vos = mediaMapper.selectThatYearTodayByUserId(userId, today.getMonthValue(), today.getDayOfMonth(), limit);

        this.setObjectDbUrl(vos);

        return vos;
    }

    public void delete(Long[] fileIds) {
        for (Long id : fileIds) {
            fileManager.removeFileLogic(id);
        }
    }
}
