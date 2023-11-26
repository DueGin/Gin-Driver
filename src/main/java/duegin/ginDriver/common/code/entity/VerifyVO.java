package duegin.ginDriver.common.code.entity;

import lombok.Data;
import lombok.experimental.Accessors;


/**
 * 验证码VO
 * @author DueGin
 */
@Data
@Accessors(chain = true)
public class VerifyVO {
    private String img;
    private String uuid;
}
