package com.ginDriver.main.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ginDriver.core.domain.vo.ResultVO;
import com.ginDriver.main.domain.po.Place;
import com.ginDriver.main.service.PlaceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * 行政区域 控制层。
 *
 * @author DueGin
 * @since 1.0
 */
@RestController
@RequestMapping("/place")
@Api(tags = "行政区域")
public class PlaceController {

    @Resource
    private PlaceService placeService;

    /**
     * 添加 行政区域
     *
     * @param place 行政区域
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("/save")
    @ApiOperation(value = "添加行政区域", notes = "添加行政区域")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = ""),
            @ApiImplicitParam(name = "name", value = "区域名称"),
            @ApiImplicitParam(name = "adcode", value = "行政区域编码，唯一"),
            @ApiImplicitParam(name = "cityAdcode", value = "县区所属的市区域编码"),
            @ApiImplicitParam(name = "provinceAdcode", value = "县区或市所属省份区域编码")})
    public ResultVO<Boolean> save(@RequestBody Place place) {
        return ResultVO.ok(placeService.save(place));
    }


    /**
     * 根据主键删除行政区域
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("/remove/{id}")
    @ApiOperation(value = "根据主键删除行政区域", notes = "根据主键删除行政区域")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "", required = true)
    })
    public ResultVO<Boolean> remove(@PathVariable Serializable id) {
        return ResultVO.ok(placeService.removeById(id));
    }


    /**
     * 根据主键更新行政区域
     *
     * @param place 行政区域
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("/update")
    @ApiOperation(value = "根据主键更新行政区域", notes = "根据主键更新行政区域")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "", required = true),
            @ApiImplicitParam(name = "name", value = "区域名称"),
            @ApiImplicitParam(name = "adcode", value = "行政区域编码，唯一"),
            @ApiImplicitParam(name = "cityAdcode", value = "县区所属的市区域编码"),
            @ApiImplicitParam(name = "provinceAdcode", value = "县区或市所属省份区域编码")})
    public ResultVO<Boolean> update(@RequestBody Place place) {
        return ResultVO.ok(placeService.updateById(place));
    }


    /**
     * 查询所有行政区域
     *
     * @return 所有数据
     */
    @GetMapping("/list")
    @ApiOperation(value = "查询所有行政区域", notes = "查询所有行政区域")
    public ResultVO<List<Place>> list() {
        return ResultVO.ok(placeService.list());
    }


    /**
     * 根据行政区域主键获取详细信息。
     *
     * @param id place主键
     * @return 行政区域详情
     */
    @GetMapping("/getInfo/{id}")
    @ApiOperation(value = "根据行政区域主键获取详细信息", notes = "根据行政区域主键获取详细信息")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "", required = true)
    })
    public ResultVO<Place> getInfo(@PathVariable Serializable id) {
        return ResultVO.ok(placeService.getById(id));
    }


    /**
     * 分页查询行政区域
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询行政区域", notes = "分页查询行政区域")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "pageNumber", value = "页码", required = true),
            @ApiImplicitParam(name = "pageSize", value = "每页大小", required = true)
    })
    public ResultVO<Page<Place>> page(Page<Place> page) {
        return ResultVO.ok(placeService.page(page));
    }
}