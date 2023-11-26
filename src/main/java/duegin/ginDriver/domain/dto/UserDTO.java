package duegin.ginDriver.domain.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author DueGin
 */
@Data
@Accessors(chain = true)
public class UserDTO {
    /**
     * 用户名
      */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 验证码uuid
     */
    private String uuid;

    /**
     * 验证码
     */
    private String verifyCode;

    /**
     * 是否记住我
     */
    private Integer isRemember;
}
