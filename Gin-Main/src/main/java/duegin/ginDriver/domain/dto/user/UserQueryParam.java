package duegin.ginDriver.domain.dto.user;

import com.ginDriver.core.domain.po.User;
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
