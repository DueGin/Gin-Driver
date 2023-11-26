package duegin.ginDriver.service;

import duegin.ginDriver.domain.model.User;

/**
 * @author DueGin
 */
public interface IUserService {
    /**
     * 登录处理
     * @param user 登录表单
     * @return token
     */
    String login(User user);
}
