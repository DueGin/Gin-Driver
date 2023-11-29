package duegin.ginDriver.service.manager;

import duegin.ginDriver.mapper.UserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author DueGin
 */
@Service
public class UserManager {

    @Resource
    private UserMapper userMapper;


}
