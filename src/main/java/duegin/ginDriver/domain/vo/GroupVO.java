package duegin.ginDriver.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author DueGin
 */
@Data
public class GroupVO {
    /**
     * 组ID
     */
    private Long groupId;

    /**
     * 组名
     */
    private String groupName;

    /**
     * 创建者用户ID
     */
    private Long userId;

    /**
     * 组角色
     */
    private String role;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
