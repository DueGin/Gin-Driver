package duegin.ginDriver.mapper;


import duegin.ginDriver.domain.model.User;
import org.apache.ibatis.annotations.Param;

/**
 * @author DueGin
 */
public interface UserMapper {

    User selectByUserId(Long userId);

    User selectByUsername(String username);

    void insert(User user);

    void insertUserRole(@Param("userId") Long userId, @Param("roleId") Long roleId);
}
