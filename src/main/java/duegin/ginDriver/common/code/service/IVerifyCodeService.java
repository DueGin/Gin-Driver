package duegin.ginDriver.common.code.service;

import javax.security.auth.login.LoginException;
import java.awt.*;

/**
 * 验证码服务
 */
public interface IVerifyCodeService {
    /**
     * 得到验证码
     *
     * @param key 当前验证码的key
     * @return 图片
     */
    Image getVerifyCodeImage(String key);

    /**
     * 从内存中得到原始验证码
     * 并去检查前端传过来的验验证码是否正确
     *
     * @param uuid 验证验的uuid
     * @param code 待校验的验证码
     */
    void verify(String uuid, String code) throws LoginException;

    /**
     * 删除验证码
     *
     * @param uuid 验证验的uuid
     */
    void deleteVerifyCode(String uuid);
}
