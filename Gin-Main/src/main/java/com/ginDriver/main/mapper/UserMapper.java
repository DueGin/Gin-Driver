package com.ginDriver.main.mapper;


import com.ginDriver.core.domain.po.User;
import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author DueGin
 */
public interface UserMapper extends BaseMapper<User> {

    User selectByUserId(Long id);

    User selectByUsername(String username);


    void insertUserRole(@Param("userId") Long userId, @Param("roleId") Long roleId);

    Boolean updateUserById(User user);

    Boolean deleteByUserId(Long id);


    ////// sys //////

    List<User> page(@Param("user") User user, @Param("pageStart") Integer pageStart, @Param("pageSize") Integer pageSize);

    Integer count(User user);


}
