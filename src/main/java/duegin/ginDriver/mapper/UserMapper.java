package duegin.ginDriver.mapper;


import duegin.ginDriver.domain.model.User;
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

    @NotNull
    void insert(User user);

    @NotNull(message = "userId和roleId不能为null")
    void insertUserRole(Long userId, Long roleId);
}
