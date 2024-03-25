package com.ginDriver.main.service.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ginDriver.main.cache.local.SysEnumCache;
import com.ginDriver.main.constant.FileType;
import com.ginDriver.main.domain.dto.dustbin.DustbinPageDTO;
import com.ginDriver.main.domain.dto.dustbin.DustbinRemoveDTO;
import com.ginDriver.main.domain.po.Dustbin;
import com.ginDriver.main.domain.po.Media;
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
import java.util.function.Function;
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

    public void remove(DustbinRemoveDTO dto) {

        FileType fileType = FileType.getFileTypeByIdx(dto.getFileType());
        if (fileType == null) {
            return;
        }

        Function<List<Long>, Boolean> removeInDbFunction = null;
        switch (fileType) {
            case media:
                removeInDbFunction = (fileIds) -> mediaService.getBaseMapper().removeByFileIds(fileIds);
                break;
        }

        remove(dto.getIds(), removeInDbFunction);
    }

    @Transactional
    public void remove(Collection<Long> ids, Function<List<Long>, Boolean> removeInDbFunction) {
        if (CollectionUtils.isEmpty(ids)) {
            return;
        }
        List<Dustbin> dustbinList = dustbinService.lambdaQuery()
                .in(Dustbin::getId, ids)
                .list();


        // 删除额外的
        if (removeInDbFunction != null) {
            List<Long> fileIds = dustbinList.stream().map(Dustbin::getFileId).collect(Collectors.toList());
            if (!fileIds.isEmpty()) {
                removeInDbFunction.apply(fileIds);
            }
        }

        // 删除垃圾箱
        dustbinService.removeByIds(ids);
    }

    public Page<DustbinVO> getDustbinPage(DustbinPageDTO page) {
        DustbinMapper dustbinMapper = dustbinService.getBaseMapper();
        dustbinMapper.getDustbinPage(page, SecurityUtils.getUserId());

        FileType fileType = FileType.getFileTypeByIdx(page.getFileType());
        if (fileType == null) {
            return new Page<>(page.getCurrent(), page.getSize());
        }

        // 设置minio url
        page.getRecords().forEach(d -> {
            String objUrl = fileService.getObjUrl(fileType.name, d.getFileName(), DUSTBIN_EXPIRE);
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
