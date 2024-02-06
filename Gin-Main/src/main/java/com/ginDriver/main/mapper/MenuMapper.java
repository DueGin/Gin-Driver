package com.ginDriver.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ginDriver.main.domain.po.Menu;
import com.ginDriver.main.domain.vo.MenuVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
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

    List<MenuVO> getUserMenuByUserId(@Param("userId") Long userId, @Param("roleIds") Collection<Long> roleIds);

    List<MenuVO> selectAllRouter();
}
