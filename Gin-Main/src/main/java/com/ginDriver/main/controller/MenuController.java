package com.ginDriver.main.controller;

import com.ginDriver.core.domain.vo.ResultVO;
import com.ginDriver.main.domain.po.Menu;
import com.ginDriver.main.domain.vo.MenuVO;
import com.ginDriver.main.service.MenuService;
import com.mybatisflex.core.paginate.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("/save")
    public boolean save(@RequestBody Menu menu) {
        return menuService.save(menu);
    }


    /**
     * 根据主键删除菜单权限表
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("/remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return menuService.removeById(id);
    }


    /**
     * 根据主键更新菜单权限表
     *
     * @param menu 菜单权限表
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("/update")
    public boolean update(@RequestBody Menu menu) {
        return menuService.updateById(menu);
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


//    @GetMapping("treeList")
//    public ResultVO<List<Menu>> treeList(){
//
//    }

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
    @GetMapping("/page")
    public ResultVO<Page<Menu>> page(Page<Menu> page) {
        return ResultVO.ok(menuService.page(page));
    }

    @GetMapping("/list/{type}")
    public ResultVO<List<MenuVO>> getListByType(@PathVariable Integer type) {
        return ResultVO.ok(menuService.getMenuByType(type));
    }

    @GetMapping("/map")
    public ResultVO<Map<String, List<MenuVO>>> getMenuMap() {
        return ResultVO.ok(menuService.getMenuMap());
    }
}