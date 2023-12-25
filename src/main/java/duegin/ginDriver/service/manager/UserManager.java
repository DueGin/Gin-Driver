package duegin.ginDriver.service.manager;

import duegin.ginDriver.domain.po.User;
import duegin.ginDriver.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author DueGin
 * 系统用户管理
 */
@Slf4j
@Service
public class UserManager {

    @Resource
    private UserMapper userMapper;

    /**
     * 新增系统用户
     * 创建用户，并分配该用户系统角色为普通用户
     *
     * @param user 新增用户信息
     */
    @Transactional
    public Boolean saveUser(User user) {
        // 插入用户表
        userMapper.insert(user, true);
        // 插入用户角色表，并给他一个默认角色（普通用户）
        userMapper.insertUserRole(user.getId(), 2L);
        return true;
    }

}
