package duegin.ginDriver.service;


import com.mybatisflex.core.query.QueryWrapper;
import duegin.ginDriver.core.service.impl.MyServiceImpl;
import duegin.ginDriver.domain.po.Menu;
import duegin.ginDriver.domain.vo.MenuVO;
import duegin.ginDriver.mapper.MenuMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 菜单权限表 服务层实现。
 *
 * @author DueGin
 * @since 1.0
 */
@Service
public class MenuService extends MyServiceImpl<MenuMapper, Menu> {

    public List<MenuVO> getMenuByType(Integer type) {
        QueryWrapper qw = QueryWrapper.create().eq(Menu::getType, type);
        List<Menu> list = super.list(qw);

        // 父菜单，同时也是res
        List<MenuVO> parentMenuList = new ArrayList<>();

        Iterator<Menu> iterator = list.iterator();
        // 父菜单
        while (iterator.hasNext()) {
            Menu menu = iterator.next();
            if (menu.getParentId() == 0) {
                MenuVO vo = new MenuVO();
                BeanUtils.copyProperties(menu, vo);
                parentMenuList.add(vo);
                iterator.remove();
            }
        }

        // 递归设置菜单
        setMenuChildrenList(parentMenuList, list);

        return parentMenuList;
    }

    /**
     * 递归处理菜单
     *
     * @param parentMenuList 父菜单列表
     * @param originMenuList 待处理菜单列表
     */
    public void setMenuChildrenList(List<MenuVO> parentMenuList, List<Menu> originMenuList) {
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
                    children.add(v);
                    iter.remove();
                }
            }
            vo.setChildrenList(children);
        }
        // 子菜单
        if (!originMenuList.isEmpty()) {
            for (MenuVO vo : parentMenuList) {
                setMenuChildrenList(vo.getChildrenList(), originMenuList);
            }
        }
    }

}