package duegin.ginDriver.mapper;


import com.mybatisflex.core.BaseMapper;
import duegin.ginDriver.domain.po.User;
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

    List<User> page(User user, Integer pageStart, Integer pageSize);

    Integer count(User user);


}
