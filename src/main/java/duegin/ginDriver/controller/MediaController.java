package duegin.ginDriver.controller;

import duegin.ginDriver.domain.po.Media;
import duegin.ginDriver.domain.vo.Result;
import duegin.ginDriver.mapper.MediaMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author DueGin
 */
@Api(tags = "媒体")
@Slf4j
@RestController
@RequestMapping("media")
public class MediaController {
    @Resource
    private MediaMapper mediaMapper;

    @ApiOperation("保存媒体")
    @GetMapping("save")
    public Result save(@RequestBody Media media) {
        mediaMapper.insertOrUpdate(media);
        return Result.ok();
    }


}
