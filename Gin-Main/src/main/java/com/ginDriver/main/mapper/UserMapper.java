package com.ginDriver.main.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ginDriver.core.domain.po.User;
import com.ginDriver.main.domain.vo.SysUserVO;
import org.apache.ibatis.annotations.Param;

/**
 * @author DueGin
 */
public interface UserMapper extends BaseMapper<User> {

    User selectByUserId(Long id);

    User selectByUsername(String username);


    void insertUserRole(@Param("userId") Long userId, @Param("roleId") Long roleId);

    void updateUserRole(@Param("userId") Long userId, @Param("roleId") Long roleId);

    Boolean updateUserById(User user);

    Boolean deleteByUserId(Long id);


    ////// sys //////

    Page<SysUserVO> page(Page<SysUserVO> page, @Param("user") User user);


}
