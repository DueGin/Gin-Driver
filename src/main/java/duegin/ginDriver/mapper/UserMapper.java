package duegin.ginDriver.mapper;


import duegin.ginDriver.domain.model.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

/**
 * @author DueGin
 */
@Validated
public interface UserMapper {
    @NotNull
    User getUserById(Long userId);

    @NotNull
    User getUserByUsername(String username);

    void insert(@NotNull User user);

    void insertUserRole(@NotNull @Param("userId") Long userId, @NotNull @Param("roleId") Long roleId);
}
