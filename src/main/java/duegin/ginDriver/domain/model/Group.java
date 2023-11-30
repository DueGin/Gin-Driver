package duegin.ginDriver.domain.model;


import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 组
 * @author DueGin
 * @TableName group
 */

@Data
public class Group implements Serializable {
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
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 软删除
     */
    private Integer deleted;

    private static final long serialVersionUID = 1L;
}