package com.ginDriver.main.service.manager;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ginDriver.main.constant.FileType;
import com.ginDriver.main.domain.dto.dustbin.DustbinPageDTO;
import com.ginDriver.main.domain.dto.dustbin.DustbinRemoveDTO;
import com.ginDriver.main.domain.po.Dustbin;
import com.ginDriver.main.domain.po.File;
import com.ginDriver.main.domain.vo.DustbinVO;
import com.ginDriver.main.file.FileManager;
import com.ginDriver.main.mapper.DustbinMapper;
import com.ginDriver.main.security.utils.SecurityUtils;
import com.ginDriver.main.service.DustbinService;
import com.ginDriver.main.service.FileService;
import com.ginDriver.main.service.Md5FileService;
import com.ginDriver.main.service.MediaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @Resource
    private Md5FileService md5FileService;

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

        DustbinManager self = (DustbinManager) AopContext.currentProxy();
        self.remove(dto.getIds(), removeInDbFunction);
    }

    @Transactional
    public void remove(Collection<Long> ids, Function<List<Long>, Boolean> removeInDbFunction) {
        if (CollectionUtils.isEmpty(ids)) {
            return;
        }
        List<Dustbin> dustbinList = dustbinService.lambdaQuery()
                .in(Dustbin::getId, ids)
                .list();


        List<Long> fileIds = dustbinList.stream().map(Dustbin::getFileId).collect(Collectors.toList());
        // 删除额外的
        if (removeInDbFunction != null) {
            if (!fileIds.isEmpty()) {
                removeInDbFunction.apply(fileIds);
            }
        }

        // 删除垃圾箱
        dustbinService.removeByIds(ids);

        // 删除file表
        List<File> list = fileService.getBaseMapper().selectAllByIds(fileIds);
        fileService.getBaseMapper().removeByIds(fileIds);

        // 统计每个md5减减次数
        Map<Long, Integer> md5Map = new HashMap<>();
        list.forEach(l -> {
            Long md5FileId = l.getMd5FileId();
            if (md5Map.containsKey(md5FileId)) {
                md5Map.put(md5FileId, md5Map.get(md5FileId) + 1);
            } else {
                md5Map.put(md5FileId, 1);
            }
        });

        // 减减md5_file
        md5Map.forEach((md5FileId, num) -> md5FileService.subRef(md5FileId, num));
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
            String objUrl = fileService.getObjUrl(fileType.name(), d.getObjectName(), DUSTBIN_EXPIRE);
            d.setUrl(objUrl);
            d.setObjectName(null);
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
