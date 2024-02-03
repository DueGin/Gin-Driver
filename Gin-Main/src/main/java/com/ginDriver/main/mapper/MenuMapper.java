package com.ginDriver.main.mapper;

import com.ginDriver.main.domain.po.Menu;
import com.ginDriver.main.domain.vo.MenuVO;
import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 菜单权限表 映射层。
 *
 * @author DueGin
 * @since 1.0
 */
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {

    List<MenuVO> getAll();

    List<MenuVO> selectAllRouter();
}
