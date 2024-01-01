package com.ginDriver.main.controller;

import com.ginDriver.core.domain.vo.ResultVO;
import com.ginDriver.main.domain.po.Dustbin;
import com.ginDriver.main.service.DustbinService;
import com.mybatisflex.core.paginate.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * 媒体资源垃圾箱 控制层。
 *
 * @author DueGin
 * @since 1.0
 */
@RestController
@RequestMapping("/dustbin")
@Api(tags = "媒体资源垃圾箱")
public class DustbinController {

    @Autowired
    private DustbinService dustbinService;

    /**
     * 添加 媒体资源垃圾箱
     *
     * @param dustbin 媒体资源垃圾箱
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("/save")
    @ApiOperation(value = "添加媒体资源垃圾箱", notes = "添加媒体资源垃圾箱")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = ""),
            @ApiImplicitParam(name = "mediaId", value = "媒体ID"),
            @ApiImplicitParam(name = "userId", value = "删除者ID"),
            @ApiImplicitParam(name = "fileName", value = "文件名"),
            @ApiImplicitParam(name = "type", value = "文件类型(1：图片，2：视频，3：电影，4：其他)"),
            @ApiImplicitParam(name = "createTime", value = "创建时间"),
            @ApiImplicitParam(name = "updateTime", value = "更新时间")})
    public ResultVO<Boolean> save(@RequestBody Dustbin dustbin) {
        return ResultVO.ok(dustbinService.save(dustbin));
    }


    /**
     * 根据主键删除媒体资源垃圾箱
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("/remove/{id}")
    @ApiOperation(value = "根据主键删除媒体资源垃圾箱", notes = "根据主键删除媒体资源垃圾箱")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "", required = true)
    })
    public ResultVO<Boolean> remove(@PathVariable Serializable id) {
        return ResultVO.ok(dustbinService.removeById(id));
    }


    /**
     * 根据主键更新媒体资源垃圾箱
     *
     * @param dustbin 媒体资源垃圾箱
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("/update")
    @ApiOperation(value = "根据主键更新媒体资源垃圾箱", notes = "根据主键更新媒体资源垃圾箱")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "", required = true),
            @ApiImplicitParam(name = "mediaId", value = "媒体ID"),
            @ApiImplicitParam(name = "userId", value = "删除者ID"),
            @ApiImplicitParam(name = "fileName", value = "文件名"),
            @ApiImplicitParam(name = "type", value = "文件类型(1：图片，2：视频，3：电影，4：其他)"),
            @ApiImplicitParam(name = "createTime", value = "创建时间"),
            @ApiImplicitParam(name = "updateTime", value = "更新时间")})
    public ResultVO<Boolean> update(@RequestBody Dustbin dustbin) {
        return ResultVO.ok(dustbinService.updateById(dustbin));
    }


    /**
     * 查询所有媒体资源垃圾箱
     *
     * @return 所有数据
     */
    @GetMapping("/list")
    @ApiOperation(value = "查询所有媒体资源垃圾箱", notes = "查询所有媒体资源垃圾箱")
    public ResultVO<List<Dustbin>> list() {
        return ResultVO.ok(dustbinService.list());
    }


    /**
     * 根据媒体资源垃圾箱主键获取详细信息。
     *
     * @param id dustbin主键
     * @return 媒体资源垃圾箱详情
     */
    @GetMapping("/getInfo/{id}")
    @ApiOperation(value = "根据媒体资源垃圾箱主键获取详细信息", notes = "根据媒体资源垃圾箱主键获取详细信息")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "", required = true)
    })
    public ResultVO<Dustbin> getInfo(@PathVariable Serializable id) {
        return ResultVO.ok(dustbinService.getById(id));
    }


    /**
     * 分页查询媒体资源垃圾箱
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询媒体资源垃圾箱", notes = "分页查询媒体资源垃圾箱")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "pageNumber", value = "页码", required = true),
            @ApiImplicitParam(name = "pageSize", value = "每页大小", required = true)
    })
    public ResultVO<Page<Dustbin>> page(Page<Dustbin> page) {
        return ResultVO.ok(dustbinService.page(page));
    }
}