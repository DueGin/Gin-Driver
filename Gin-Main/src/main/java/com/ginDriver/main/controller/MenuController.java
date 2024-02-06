package com.ginDriver.main.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ginDriver.core.domain.vo.ResultVO;
import com.ginDriver.core.exception.ApiException;
import com.ginDriver.main.domain.dto.menu.MenuDTO;
import com.ginDriver.main.domain.po.Menu;
import com.ginDriver.main.domain.vo.MenuVO;
import com.ginDriver.main.service.MenuService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 菜单权限表 控制层。
 *
 * @author DueGin
 * @since 1.0
 */
@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    /**
     * 添加 菜单权限表
     *
     * @param menu 菜单权限表
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/save")
    public void save(@RequestBody Menu menu) {

        menuService.save(menu);
    }


    /**
     * 根据主键删除菜单权限表
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/remove/{id}")
    public void remove(@PathVariable Serializable id) {
        menuService.removeById(id);
    }


    /**
     * 根据主键更新菜单权限表
     *
     * @param dto 菜单权限表
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update")
    public void update(@RequestBody @Valid MenuDTO dto) {
        if (dto.getParentId() != null && dto.getId() != null && dto.getParentId().equals(dto.getId())) {
            throw new ApiException("🤨不是，你自己做自己老爸是吧");
        }
        Menu menu = new Menu();
        BeanUtils.copyProperties(dto, menu);
        menuService.updateById(menu);
    }


    /**
     * 查询所有菜单权限表
     *
     * @return 所有数据
     */
    @GetMapping("/list")
    public ResultVO<List<Menu>> list() {
        return ResultVO.ok(menuService.list());
    }

    /**
     * 根据菜单权限表主键获取详细信息。
     *
     * @param id menu主键
     * @return 菜单权限表详情
     */
    @GetMapping("/getInfo/{id}")
    public ResultVO<Menu> getInfo(@PathVariable Serializable id) {
        return ResultVO.ok(menuService.getById(id));
    }


    /**
     * 分页查询菜单权限表
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/page")
    public ResultVO<Page<Menu>> page(Page<Menu> page) {
        return ResultVO.ok(menuService.page(page));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/list/{type}")
    public ResultVO<List<MenuVO>> getListByType(@PathVariable Long type) {
        return ResultVO.ok(menuService.getMenuListByType(type));
    }

    @GetMapping("/map/user")
    public ResultVO<Map<String, List<MenuVO>>> getUserMenuMap() {
        return ResultVO.ok(menuService.getUserMenuMap());
    }

    @GetMapping("/router")
    public ResultVO<Map<Long, List<MenuVO>>> router() {
        return ResultVO.ok(menuService.getFlatMapGroupByLayoutComponent());
    }
}