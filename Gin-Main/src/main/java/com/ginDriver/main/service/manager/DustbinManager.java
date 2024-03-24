package com.ginDriver.main.service.manager;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ginDriver.main.cache.local.SysEnumCache;
import com.ginDriver.main.domain.dto.dustbin.DustbinPageDTO;
import com.ginDriver.main.domain.po.Dustbin;
import com.ginDriver.main.domain.po.SysEnum;
import com.ginDriver.main.domain.vo.DustbinVO;
import com.ginDriver.main.file.FileManager;
import com.ginDriver.main.mapper.DustbinMapper;
import com.ginDriver.main.security.utils.SecurityUtils;
import com.ginDriver.main.service.DustbinService;
import com.ginDriver.main.service.FileService;
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
    private DustbinService dustbinService;

    @Resource
    private FileService fileService;

    @Resource
    private FileManager fileManager;

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
        List<Long> fileIds = dustbinList.stream().map(Dustbin::getFileId).collect(Collectors.toList());
        mediaService.removeByIds(fileIds);

        // 删除垃圾箱
        dustbinService.removeByIds(ids);
    }

    public Page<DustbinVO> getDustbinPage(DustbinPageDTO page) {
        DustbinMapper dustbinMapper = dustbinService.getBaseMapper();
        dustbinMapper.getDustbinPage(page, SecurityUtils.getUserId());

        SysEnum sysEnum = SysEnumCache.getEnumById(page.getFileTypeId());

        // 设置minio url
        page.getRecords().forEach(d -> {
            String objUrl = fileService.getObjUrl(sysEnum.getName(), d.getFileName(), DUSTBIN_EXPIRE);
            d.setUrl(objUrl);
        });

        return page;
    }

    /**
     * 撤销媒体删除
     *
     * @param ids 垃圾箱主键集合
     */
    public void reborn(Collection<Long> ids) {
        fileManager.rebornFileBatch(ids);
    }
}
