package duegin.ginDriver.mapper;


import duegin.ginDriver.domain.model.User;
import org.springframework.stereotype.Repository;

/**
 * @author DueGin
 */
@Repository
public interface UserMapper {
User getUserById(Long userId);

    User getUserByUsername(String username);

    void insert(User user);
}
