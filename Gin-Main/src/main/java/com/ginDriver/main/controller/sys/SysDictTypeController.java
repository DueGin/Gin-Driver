package com.ginDriver.main.controller.sys;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ginDriver.core.domain.vo.ResultVO;
import com.ginDriver.core.result.BusinessController;
import com.ginDriver.main.domain.dto.sys.dict.SysDictTypePageDTO;
import com.ginDriver.main.domain.po.SysDictType;
import com.ginDriver.main.service.SysDictTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * 控制层。
 *
 * @author DueGin
 * @since 1.0
 */
@BusinessController
@RequestMapping("/sys/dictType")
@Api(tags = "")
@PreAuthorize("hasRole('ADMIN')")
public class SysDictTypeController {

    @Autowired
    private SysDictTypeService sysDictTypeService;

    /**
     * 添加
     *
     * @param sysDictType
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("/save")
    @ApiOperation(value = "添加", notes = "添加")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = ""),
            @ApiImplicitParam(name = "dictName", value = "字典名称"),
            @ApiImplicitParam(name = "status", value = "状态（0正常 1停用）"),
            @ApiImplicitParam(name = "remark", value = "备注"),
            @ApiImplicitParam(name = "createTime", value = "创建时间"),
            @ApiImplicitParam(name = "updateTime", value = "更新时间")
    })
    public ResultVO<Boolean> save(@RequestBody SysDictType sysDictType) {
        return ResultVO.ok(sysDictTypeService.save(sysDictType));
    }


    /**
     * 根据主键删除
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("/remove/{id}")
    @ApiOperation(value = "根据主键删除", notes = "根据主键删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "", required = true)
    })
    public ResultVO<Boolean> remove(@PathVariable Serializable id) {
        return ResultVO.ok(sysDictTypeService.removeById(id));
    }


    /**
     * 根据主键更新
     *
     * @param sysDictType
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("/update")
    @ApiOperation(value = "根据主键更新", notes = "根据主键更新")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "", required = true),
            @ApiImplicitParam(name = "dictName", value = "字典名称"),
            @ApiImplicitParam(name = "status", value = "状态（0正常 1停用）"),
            @ApiImplicitParam(name = "remark", value = "备注"),
            @ApiImplicitParam(name = "createTime", value = "创建时间"),
            @ApiImplicitParam(name = "updateTime", value = "更新时间")
    })
    public ResultVO<Boolean> update(@RequestBody SysDictType sysDictType) {
        return ResultVO.ok(sysDictTypeService.updateById(sysDictType));
    }


    /**
     * 查询所有
     *
     * @return 所有数据
     */
    @GetMapping("/list")
    @ApiOperation(value = "查询所有", notes = "查询所有")
    public ResultVO<List<SysDictType>> list() {
        return ResultVO.ok(sysDictTypeService.list());
    }


    /**
     * 根据主键获取详细信息。
     *
     * @param id sysDictType主键
     * @return 详情
     */
    @GetMapping("/getInfo/{id}")
    @ApiOperation(value = "根据主键获取详细信息", notes = "根据主键获取详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "", required = true)
    })
    public ResultVO<SysDictType> getInfo(@PathVariable Serializable id) {
        return ResultVO.ok(sysDictTypeService.getById(id));
    }


    /**
     * 分页查询
     *
     * @param pageDTO 分页对象
     * @return 分页对象
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNumber", value = "页码", required = true),
            @ApiImplicitParam(name = "pageSize", value = "每页大小", required = true)
    })
    public ResultVO<Page<SysDictType>> page(SysDictTypePageDTO pageDTO) {
        LambdaQueryWrapper<SysDictType> qw = new QueryWrapper<SysDictType>().lambda()
                .eq(StringUtils.isNotBlank(pageDTO.getCode()), SysDictType::getCode, pageDTO.getCode())
                .eq(StringUtils.isNotBlank(pageDTO.getName()), SysDictType::getName, pageDTO.getName())
                .eq(pageDTO.getStatus() != null, SysDictType::getStatus, pageDTO.getStatus());

        return ResultVO.ok(sysDictTypeService.page(pageDTO, qw));
    }
}