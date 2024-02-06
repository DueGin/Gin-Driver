package com.ginDriver.main.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ginDriver.core.domain.vo.ResultVO;
import com.ginDriver.main.domain.po.MediaExif;
import com.ginDriver.main.service.MediaExifService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * 媒体资源信息 控制层。
 *
 * @author DueGin
 * @since 1.0
 */
@RestController
@RequestMapping("/mediaExif")
@Api(tags = "媒体资源信息")
public class MediaExifController {

    @Autowired
    private MediaExifService mediaExifService;

    /**
     * 添加 媒体资源信息
     *
     * @param mediaExif 媒体资源信息
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("/save")
    @ApiOperation(value = "添加媒体资源信息", notes = "添加媒体资源信息")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = ""),
            @ApiImplicitParam(name = "mediaId", value = "文件id"),
            @ApiImplicitParam(name = "width", value = ""),
            @ApiImplicitParam(name = "height", value = ""),
            @ApiImplicitParam(name = "mimeType", value = ""),
            @ApiImplicitParam(name = "createTime", value = ""),
            @ApiImplicitParam(name = "model", value = "")})
    public ResultVO<Boolean> save(@RequestBody MediaExif mediaExif) {
        return ResultVO.ok(mediaExifService.save(mediaExif));
    }


    /**
     * 根据主键删除媒体资源信息
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("/remove/{id}")
    @ApiOperation(value = "根据主键删除媒体资源信息", notes = "根据主键删除媒体资源信息")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "", required = true)
    })
    public ResultVO<Boolean> remove(@PathVariable Serializable id) {
        return ResultVO.ok(mediaExifService.removeById(id));
    }


    /**
     * 根据主键更新媒体资源信息
     *
     * @param mediaExif 媒体资源信息
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("/update")
    @ApiOperation(value = "根据主键更新媒体资源信息", notes = "根据主键更新媒体资源信息")
    public ResultVO<Boolean> update(@RequestBody MediaExif mediaExif) {
        return ResultVO.ok(mediaExifService.updateById(mediaExif));
    }


    /**
     * 查询所有媒体资源信息
     *
     * @return 所有数据
     */
    @GetMapping("/list")
    @ApiOperation(value = "查询所有媒体资源信息", notes = "查询所有媒体资源信息")
    public ResultVO<List<MediaExif>> list() {
        return ResultVO.ok(mediaExifService.list());
    }


    /**
     * 根据媒体资源信息主键获取详细信息。
     *
     * @param id mediaExif主键
     * @return 媒体资源信息详情
     */
    @GetMapping("/getInfo/{id}")
    @ApiOperation(value = "根据媒体资源信息主键获取详细信息", notes = "根据媒体资源信息主键获取详细信息")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "", required = true)
    })
    public ResultVO<MediaExif> getInfo(@PathVariable Serializable id) {
        return ResultVO.ok(mediaExifService.getById(id));
    }


    /**
     * 分页查询媒体资源信息
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询媒体资源信息", notes = "分页查询媒体资源信息")
    public ResultVO<Page<MediaExif>> page(Page<MediaExif> page) {
        return ResultVO.ok(mediaExifService.page(page));
    }
}