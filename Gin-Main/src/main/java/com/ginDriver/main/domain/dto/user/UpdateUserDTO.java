package com.ginDriver.main.domain.dto.user;

import com.ginDriver.core.domain.po.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author DueGin
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UpdateUserDTO extends User {
    private Long sysRoleId;
}
