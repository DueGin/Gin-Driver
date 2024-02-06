package com.ginDriver.main.controller.sys;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ginDriver.core.domain.vo.ResultVO;
import com.ginDriver.core.result.BusinessController;
import com.ginDriver.main.domain.dto.layoutComponent.LayoutComponentPageDTO;
import com.ginDriver.main.domain.po.LayoutComponent;
import com.ginDriver.main.service.LayoutComponentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 组件布局 控制层。
 *
 * @author DueGin
 * @since 1.0
 */
@BusinessController
@RequestMapping("/sys/layoutComponent")
@Api(tags = "组件布局")
public class LayoutComponentController {

    @Resource
    private LayoutComponentService layoutComponentService;

    /**
     * 添加 组件布局
     *
     * @param layoutComponent 组件布局
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/save")
    @ApiOperation(value = "添加组件布局", notes = "添加组件布局")
    public ResultVO<Boolean> save(@RequestBody LayoutComponent layoutComponent) {
        layoutComponent.setCreateTime(null);
        layoutComponent.setUpdateTime(null);
        return ResultVO.ok(layoutComponentService.save(layoutComponent));
    }


    /**
     * 根据主键删除组件布局
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/remove/{id}")
    @ApiOperation(value = "根据主键删除组件布局", notes = "根据主键删除组件布局")
    public ResultVO<Boolean> remove(@PathVariable Serializable id) {
        return ResultVO.ok(layoutComponentService.removeById(id));
    }


    /**
     * 根据主键更新组件布局
     *
     * @param layoutComponent 组件布局
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update")
    @ApiOperation(value = "根据主键更新组件布局", notes = "根据主键更新组件布局")
    public ResultVO<Boolean> update(@RequestBody LayoutComponent layoutComponent) {
        layoutComponent.setCreateTime(null);
        layoutComponent.setUpdateTime(null);
        return ResultVO.ok(layoutComponentService.updateById(layoutComponent));
    }


    /**
     * 查询所有组件布局
     *
     * @return 所有数据
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/list")
    @ApiOperation(value = "查询所有组件布局", notes = "查询所有组件布局")
    public ResultVO<List<LayoutComponent>> list() {
        return ResultVO.ok(layoutComponentService.list());
    }

    @GetMapping("map")
    public ResultVO<Map<Long, LayoutComponent>> map(String idsStr) {
        List<Long> ids = null;
        if (StringUtils.isNotBlank(idsStr)) {
            String[] split = idsStr.split(",");
            if (split.length != 0) {
                ids = new ArrayList<>(split.length);
                for (String id : split) {
                    ids.add(Long.valueOf(id));
                }
            }
        }
        return ResultVO.ok(layoutComponentService.getLayoutComponentMap(ids));
    }

    /**
     * 根据组件布局主键获取详细信息。
     *
     * @param id layoutComponent主键
     * @return 组件布局详情
     */
    @GetMapping("/getInfo/{id}")
    @ApiOperation(value = "根据组件布局主键获取详细信息", notes = "根据组件布局主键获取详细信息")
    public ResultVO<LayoutComponent> getInfo(@PathVariable Serializable id) {
        return ResultVO.ok(layoutComponentService.getById(id));
    }


    /**
     * 分页查询组件布局
     *
     * @param dto 分页对象
     * @return 分页对象
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/page")
    @ApiOperation(value = "分页查询组件布局", notes = "分页查询组件布局")
    public ResultVO<IPage<LayoutComponent>> page(LayoutComponentPageDTO dto) {
        LambdaQueryWrapper<LayoutComponent> qw = new QueryWrapper<LayoutComponent>().lambda()
                .like(StringUtils.isNotBlank(dto.getName()), LayoutComponent::getName, dto.getName())
                .like(StringUtils.isNotBlank(dto.getPath()), LayoutComponent::getPath, dto.getPath())
                .eq(dto.getHasSlot() != null, LayoutComponent::getHasSlot, dto.getHasSlot())
                .eq(dto.getHasRouter() != null, LayoutComponent::getHasRouter, dto.getHasRouter());
        layoutComponentService.page(dto, qw);
        return ResultVO.ok(dto);
    }
}