package duegin.ginDriver.domain.dto.group;

import lombok.Data;

/**
 * @author DueGin
 */
@Data
public class AddGroupDTO {

    /**
     * 组名
     */
    private String groupName;

    /**
     * 简介
     */
    private String description;

    /**
     * 头像
     */
    private String avatar;

}
