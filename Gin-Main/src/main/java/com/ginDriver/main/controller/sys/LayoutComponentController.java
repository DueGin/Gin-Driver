package com.ginDriver.main.controller.sys;

import com.ginDriver.core.domain.vo.ResultVO;
import com.ginDriver.main.domain.dto.layoutComponent.LayoutComponentPageDTO;
import com.ginDriver.main.domain.po.LayoutComponent;
import com.ginDriver.main.service.LayoutComponentService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
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
@RestController
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
    @PostMapping("/save")
    @ApiOperation(value = "添加组件布局", notes = "添加组件布局")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = ""),
            @ApiImplicitParam(name = "name", value = "组件名称"),
            @ApiImplicitParam(name = "path", value = "组件路径"),
            @ApiImplicitParam(name = "hasSlot", value = "是否存在插槽，0:不存在，1:存在"),
            @ApiImplicitParam(name = "hasRouter", value = "是否存在路由插槽，0:不存在，1:存在"),
            @ApiImplicitParam(name = "createTime", value = "创建时间"),
            @ApiImplicitParam(name = "updateTime", value = "更新时间"),
            @ApiImplicitParam(name = "remark", value = "备注")})
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
    @DeleteMapping("/remove/{id}")
    @ApiOperation(value = "根据主键删除组件布局", notes = "根据主键删除组件布局")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "", required = true)
    })
    public ResultVO<Boolean> remove(@PathVariable Serializable id) {
        return ResultVO.ok(layoutComponentService.removeById(id));
    }


    /**
     * 根据主键更新组件布局
     *
     * @param layoutComponent 组件布局
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("/update")
    @ApiOperation(value = "根据主键更新组件布局", notes = "根据主键更新组件布局")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "", required = true),
            @ApiImplicitParam(name = "name", value = "组件名称"),
            @ApiImplicitParam(name = "path", value = "组件路径"),
            @ApiImplicitParam(name = "hasSlot", value = "是否存在插槽，0:不存在，1:存在"),
            @ApiImplicitParam(name = "hasRouter", value = "是否存在路由插槽，0:不存在，1:存在"),
            @ApiImplicitParam(name = "createTime", value = "创建时间"),
            @ApiImplicitParam(name = "updateTime", value = "更新时间"),
            @ApiImplicitParam(name = "remark", value = "备注")})
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
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "", required = true)
    })
    public ResultVO<LayoutComponent> getInfo(@PathVariable Serializable id) {
        return ResultVO.ok(layoutComponentService.getById(id));
    }


    /**
     * 分页查询组件布局
     *
     * @param dto 分页对象
     * @return 分页对象
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询组件布局", notes = "分页查询组件布局")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "pageNumber", value = "页码", required = true),
            @ApiImplicitParam(name = "pageSize", value = "每页大小", required = true)
    })
    public ResultVO<Page<LayoutComponent>> page(LayoutComponentPageDTO dto) {
        QueryWrapper qw = QueryWrapper.create().from(LayoutComponent.class)
                .like(LayoutComponent::getName, dto.getName(), StringUtils.isNotBlank(dto.getName()))
                .like(LayoutComponent::getPath, dto.getPath(), StringUtils.isNotBlank(dto.getPath()))
                .eq(LayoutComponent::getHasSlot, dto.getHasSlot(), dto.getHasSlot() != null)
                .eq(LayoutComponent::getHasRouter, dto.getHasRouter(), dto.getHasRouter() != null);
        Page<LayoutComponent> page = layoutComponentService.page(dto, qw);
        return ResultVO.ok(page);
    }
}