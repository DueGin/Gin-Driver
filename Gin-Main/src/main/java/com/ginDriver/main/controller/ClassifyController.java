package com.ginDriver.main.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ginDriver.core.domain.vo.ResultVO;
import com.ginDriver.main.domain.vo.ClassifyVO;
import com.ginDriver.main.domain.vo.MediaVO;
import com.ginDriver.main.service.MediaClassifyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 分类
 *
 * @author DueGin
 */
@RestController
@Slf4j
@RequestMapping("/media/classify")
public class ClassifyController {

    @Resource
    private MediaClassifyService mediaClassifyService;

    @GetMapping("list")
    public ResultVO getListByType(String type) {
        log.info(type);
        List<ClassifyVO> classifyFolderList = mediaClassifyService.getClassifyFolderList(type);
        return ResultVO.ok(classifyFolderList);
    }

    @GetMapping("page/{type}/{classifyId}")
    public ResultVO<Page<MediaVO>> getPageByClassifyId(Page<MediaVO> page, @PathVariable String classifyId, @PathVariable String type){
        log.info("type: {}, classifyId: {}", type, classifyId);
        Page<MediaVO> list = mediaClassifyService.getDetailPageByClassifyId(page, type, classifyId);
        return ResultVO.ok(list);
    }

}
