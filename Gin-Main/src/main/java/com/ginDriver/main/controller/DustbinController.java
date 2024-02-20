package com.ginDriver.main.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ginDriver.core.domain.vo.ResultVO;
import com.ginDriver.core.exception.ApiException;
import com.ginDriver.core.result.BusinessController;
import com.ginDriver.main.domain.po.Dustbin;
import com.ginDriver.main.domain.vo.DustbinVO;
import com.ginDriver.main.service.DustbinService;
import com.ginDriver.main.service.manager.DustbinManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 媒体资源垃圾箱 控制层。
 *
 * @author DueGin
 * @since 1.0
 */
@BusinessController
@RequestMapping("/media/dustbin")
@Api(tags = "媒体资源垃圾箱")
public class DustbinController {

    @Autowired
    private DustbinService dustbinService;

    @Resource
    private DustbinManager dustbinManager;

    /**
     * 添加 媒体资源垃圾箱
     *
     * @param dustbin 媒体资源垃圾箱
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("/save")
    @ApiOperation(value = "添加媒体资源垃圾箱", notes = "添加媒体资源垃圾箱")
    public ResultVO<Boolean> save(@RequestBody Dustbin dustbin) {
        return ResultVO.ok(dustbinService.save(dustbin));
    }


    /**
     * 根据主键删除媒体资源垃圾箱
     *
     * @param ids 主键集合
     */
    @PostMapping("/remove")
    @ApiOperation(value = "根据主键删除媒体资源垃圾箱", notes = "根据主键删除媒体资源垃圾箱")
    public void remove(@RequestBody Long[] ids) {
        dustbinManager.remove(List.of(ids));
    }


    /**
     * 根据主键更新媒体资源垃圾箱
     *
     * @param dustbin 媒体资源垃圾箱
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("/update")
    @ApiOperation(value = "根据主键更新媒体资源垃圾箱", notes = "根据主键更新媒体资源垃圾箱")
    public void update(@RequestBody @Valid Dustbin dustbin) {
        dustbinService.updateById(dustbin);
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
    public ResultVO<Page<DustbinVO>> page(Page<DustbinVO> page) {
        return ResultVO.ok(dustbinManager.getDustbinPage(page));
    }

    @PostMapping("/reborn")
    @ApiOperation(value = "根据主键还原媒体资源", notes = "根据主键还原媒体资源")
    public void reborn(@RequestBody @NotNull Long[] ids) {
        if (ids == null || ids.length == 0) {
            throw new ApiException("ids不能为空");
        }
        dustbinManager.reborn(List.of(ids));
    }
}