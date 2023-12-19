package duegin.ginDriver.mapper;


import duegin.ginDriver.domain.po.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author DueGin
 */
public interface UserMapper {

    User selectByUserId(Long id);

    User selectByUsername(String username);

    void insert(User user);

    void insertUserRole(@Param("userId") Long userId, @Param("roleId") Long roleId);

    Boolean updateUserById(User user);

    Boolean deleteByUserId(Long id);


    ////// sys //////

    List<User> page(User user, Integer pageStart, Integer pageSize);

    Integer count(User user);


}
