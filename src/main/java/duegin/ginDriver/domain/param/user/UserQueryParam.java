package duegin.ginDriver.domain.param.user;

import duegin.ginDriver.domain.model.User;
import lombok.Data;
import lombok.ToString;

/**
 * @author DueGin
 */
@Data
@ToString(callSuper = true)
public class UserQueryParam extends User {
    private Integer pageNum;
    private Integer pageSize;
}
