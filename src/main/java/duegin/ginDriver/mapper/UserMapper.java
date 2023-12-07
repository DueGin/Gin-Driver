package duegin.ginDriver.mapper;


import duegin.ginDriver.domain.model.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author DueGin
 */
public interface UserMapper {

    User selectByUserId(Long userId);

    User selectByUsername(String username);

    void insert(User user);

    void insertUserRole(@Param("userId") Long userId, @Param("roleId") Long roleId);

    Boolean updateUserById(User user);

    Boolean deleteByUserId(Long userId);


    ////// sys //////

    List<User> page(User user, Integer pageStart, Integer pageSize);

    Integer count(User user);


}
