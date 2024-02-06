package com.ginDriver.main.service;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ginDriver.core.domain.bo.UserBO;
import com.ginDriver.core.exception.ApiException;
import com.ginDriver.core.service.impl.MyServiceImpl;
import com.ginDriver.main.domain.po.LayoutComponent;
import com.ginDriver.main.domain.po.Menu;
import com.ginDriver.main.domain.po.Role;
import com.ginDriver.main.domain.po.SysDict;
import com.ginDriver.main.domain.vo.MenuVO;
import com.ginDriver.main.mapper.MenuMapper;
import com.ginDriver.main.security.service.RoleService;
import com.ginDriver.main.security.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 菜单权限表 服务层实现。
 *
 * @author DueGin
 * @since 1.0
 */
@Slf4j
@Service
public class MenuService extends MyServiceImpl<MenuMapper, Menu> {

    @Resource
    private LayoutComponentService layoutComponentService;

    @Resource
    private SysDictService sysDictService;

    @Resource
    private RoleService roleService;

    public List<MenuVO> getMenuListByType(Long type) {
        LambdaQueryWrapper<Menu> qw = new QueryWrapper<Menu>().lambda().eq(Menu::getType, type);
        List<Menu> list = super.list(qw);

        // 布局组件
        Set<Long> layoutComponentIds = list.stream().map(Menu::getLayoutComponentId).collect(Collectors.toSet());
        Map<Long, LayoutComponent> longLayoutComponentMap = layoutComponentService.getLayoutComponentMap(layoutComponentIds);

        // 角色
        Map<Long, Role> roleMap = roleService.getRoleMap();

        // 字典类型
        Map<Long, SysDict> menuMap = sysDictService.getDictIdMapByDictType("menu");

        // 父菜单，同时也是res
        List<MenuVO> parentMenuList = new ArrayList<>();

        Iterator<Menu> iterator = list.iterator();
        // 父菜单
        while (iterator.hasNext()) {
            Menu menu = iterator.next();
            if (menu.getParentId() == 0) {
                MenuVO vo = new MenuVO();
                BeanUtils.copyProperties(menu, vo);
                vo.setLayoutComponent(longLayoutComponentMap.get(menu.getLayoutComponentId()));

                // 字典
                SysDict sysDict = menuMap.get(menu.getType());
                if (sysDict != null) {
                    vo.setTypeName(sysDict.getLabel());
                }

                // 角色
                Role role = roleMap.get(menu.getRoleId());
                if (role != null) {
                    vo.setRoleName(role.getRoleName());
                }

                parentMenuList.add(vo);
                iterator.remove();
            }
        }

        // 递归设置菜单
        setMenuChildrenList(parentMenuList, list, longLayoutComponentMap, menuMap, roleMap);

        return parentMenuList;
    }

    /**
     * 递归处理菜单
     *
     * @param parentMenuList 父菜单列表
     * @param originMenuList 待处理菜单列表
     * @param roleMap 角色集合
     */
    private void setMenuChildrenList(List<MenuVO> parentMenuList, List<Menu> originMenuList, Map<Long, LayoutComponent> longLayoutComponentMap, Map<Long, SysDict> menuMap, Map<Long, Role> roleMap) {
        // 父菜单
        for (MenuVO vo : parentMenuList) {
            Long id = vo.getId();
            Iterator<Menu> iter = originMenuList.iterator();
            List<MenuVO> children = new ArrayList<>();
            while (iter.hasNext()) {
                Menu m = iter.next();
                if (m.getParentId().equals(id)) {
                    MenuVO v = new MenuVO();
                    BeanUtils.copyProperties(m, v);

                    // 布局组件
                    v.setLayoutComponent(longLayoutComponentMap.get(m.getLayoutComponentId()));

                    // 字典
                    SysDict sysDict = menuMap.get(m.getType());
                    if (sysDict != null) {
                        vo.setTypeName(sysDict.getLabel());
                    }

                    // 角色
                    Role role = roleMap.get(m.getRoleId());
                    if (role != null) {
                        vo.setRoleName(role.getRoleName());
                    }

                    children.add(v);
                    iter.remove();
                }
            }
            vo.setChildren(children);
        }
        // 子菜单
        if (!originMenuList.isEmpty()) {
            for (MenuVO vo : parentMenuList) {
                setMenuChildrenList(vo.getChildren(), originMenuList, longLayoutComponentMap, menuMap, roleMap);
            }
        }
    }


    /**
     * 递归处理菜单
     *
     * @param parentMenuList 父菜单列表
     * @param originMenuList 待处理菜单列表
     * @param roleMap        角色集合
     */
    private void setMenuVoChildrenList(List<MenuVO> parentMenuList, List<MenuVO> originMenuList, Map<Long, String> roleMap) {
        // 父菜单
        for (MenuVO vo : parentMenuList) {
            Long id = vo.getId();

            Iterator<MenuVO> iter = originMenuList.iterator();
            List<MenuVO> children = new ArrayList<>();
            while (iter.hasNext()) {
                MenuVO m = iter.next();
                if (m.getRoleId() != null) {
                    m.setRoleName(roleMap.get(m.getRoleId()));
                }
                if (m.getParentId().equals(id)) {
                    children.add(m);
                    iter.remove();
                }
            }
            vo.setChildren(children);
        }
        // 子菜单
        if (!originMenuList.isEmpty()) {
            for (MenuVO vo : parentMenuList) {
                setMenuVoChildrenList(vo.getChildren(), originMenuList, roleMap);
            }
        }
    }

    /**
     * 获取菜单KV集合
     *
     * @return <字典类型名称, [..MenuVO]>
     */
    public Map<String, List<MenuVO>> getUserMenuMap() {
        UserBO userBO = SecurityUtils.getLoginUser();
        if (userBO == null || userBO.getId() == null) {
            throw new ApiException("登录状态异常！");
        }

        MenuMapper mapper = super.getBaseMapper();
        List<Role> roles = roleService.getYouAndUnderRole(userBO.getRoleMap().get("sys"));
        List<Long> roleIds = roles.stream().map(Role::getId).collect(Collectors.toList());
        List<MenuVO> voList = mapper.getUserMenuByUserId(userBO.getId(), roleIds);

        // 装入roleName
        Map<Long, String> roleMap = roles.stream().collect(Collectors.toMap(Role::getId, Role::getRoleName));

        Map<String, List<MenuVO>> map = new HashMap<>();
        Set<Long> layoutComponentIds = voList.stream()
                .map(MenuVO::getLayoutComponentId)
                .collect(Collectors.toSet());

        Map<Long, LayoutComponent> layoutComponentMap = layoutComponentService.getLayoutComponentMap(layoutComponentIds);

        voList.stream()
                .collect(Collectors.groupingBy(MenuVO::getTypeName))
                .forEach((k, v) -> {
                    // 父菜单，同时也是res
                    List<MenuVO> parentMenuList = new ArrayList<>();

                    Iterator<MenuVO> iterator = v.iterator();
                    while (iterator.hasNext()) {
                        MenuVO m = iterator.next();
                        // 父菜单
                        if (m.getParentId() == 0) {
                            parentMenuList.add(m);
                            iterator.remove();
                        }

                        // 设置布局组件
                        m.setLayoutComponent(layoutComponentMap.get(m.getLayoutComponentId()));
                        if (m.getRoleId() != null) {
                            m.setRoleName(roleMap.get(m.getRoleId()));
                        }
                    }

                    // 递归设置菜单
                    setMenuVoChildrenList(parentMenuList, v, roleMap);
                    map.put(k, parentMenuList);
                });

        return map;
    }

    /**
     * 获取基于布局组件分组的map，这里目前用于前端构建动态路由
     *
     * @return {布局组件的路径, 扁平化的菜单VO列表}
     */
    public Map<Long, List<MenuVO>> getFlatMapGroupByLayoutComponent() {
        MenuMapper mapper = super.getBaseMapper();
        List<MenuVO> voList = mapper.selectAllRouter();

        Set<Long> layoutComponentIds = voList.stream()
                .map(MenuVO::getLayoutComponentId)
                .collect(Collectors.toSet());

        Map<Long, LayoutComponent> layoutComponentMap = layoutComponentService.getLayoutComponentMap(layoutComponentIds);

        List<MenuVO> flatList = getFlatList(voList);

        Map<Long, List<MenuVO>> map = flatList.stream()
                .peek(m -> m.setLayoutComponent(layoutComponentMap.get(m.getLayoutComponentId())))
                // 根据菜单的url路径去重
                .filter(distinctByKey(MenuVO::getPath))
                .collect(Collectors.groupingBy(m -> m.getLayoutComponent().getId()));

        return map;
    }

    static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    private List<MenuVO> getFlatList(List<MenuVO> voList) {
        List<MenuVO> vos = new ArrayList<>();
        for (MenuVO vo : voList) {
            List<MenuVO> children = vo.getChildren();
            vos.add(vo);
            if (!CollectionUtils.isEmpty(children)) {
                List<MenuVO> childrenList = getFlatList(children);
                vos.addAll(childrenList);
            }
        }
        return vos;
    }

}