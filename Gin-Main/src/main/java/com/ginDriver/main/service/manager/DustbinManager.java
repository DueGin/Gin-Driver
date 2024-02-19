package com.ginDriver.main.service.manager;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ginDriver.main.domain.po.Dustbin;
import com.ginDriver.main.domain.po.MediaExif;
import com.ginDriver.main.domain.vo.DustbinVO;
import com.ginDriver.main.mapper.DustbinMapper;
import com.ginDriver.main.security.utils.SecurityUtils;
import com.ginDriver.main.service.DustbinService;
import com.ginDriver.main.service.FileService;
import com.ginDriver.main.service.MediaExifService;
import com.ginDriver.main.service.MediaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author DueGin
 */
@Slf4j
@Service
public class DustbinManager {

    @Resource
    private MediaService mediaService;

    @Resource
    private MediaExifService mediaExifService;

    @Resource
    private DustbinService dustbinService;

    @Resource
    private FileService fileService;

    private static final Integer DUSTBIN_EXPIRE = 24 * 60 * 60;


    @Transactional
    public void remove(Collection<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return;
        }
        List<Dustbin> dustbinList = dustbinService.lambdaQuery()
                .in(Dustbin::getId, ids)
                .list();


        // 删除媒体表
        List<Long> mediaIds = dustbinList.stream().map(Dustbin::getMediaId).collect(Collectors.toList());
        mediaService.removeByIds(mediaIds);


        // 删除媒体信息表
        LambdaQueryWrapper<MediaExif> qw = new QueryWrapper<MediaExif>().lambda().in(MediaExif::getMediaId, mediaIds);
        mediaExifService.remove(qw);

        // 删除垃圾箱
        dustbinService.removeByIds(ids);
    }

    public Page<DustbinVO> getDustbinPage(Page<DustbinVO> page) {
        DustbinMapper dustbinMapper = dustbinService.getBaseMapper();
        dustbinMapper.getDustbinPage(page, SecurityUtils.getUserId());

        // 设置minio url
        page.getRecords().forEach(d -> {
            String objUrl = fileService.getObjUrl(FileService.FileType.media, d.getFileName(), DUSTBIN_EXPIRE);
            d.setUrl(objUrl);
        });

        return page;
    }

    /**
     * 撤销媒体删除
     *
     * @param ids 垃圾箱主键集合
     */
    @Transactional
    public void reborn(Collection<Long> ids) {
        List<Long> mediaIds = dustbinService.lambdaQuery()
                .in(Dustbin::getId, ids)
                .list()
                .stream()
                .map(Dustbin::getMediaId)
                .collect(Collectors.toList());

        if (!CollectionUtils.isEmpty(mediaIds)) {
            mediaService.getBaseMapper().updateMediaDeleted(mediaIds);
            dustbinService.removeByIds(ids);
        }
    }
}
