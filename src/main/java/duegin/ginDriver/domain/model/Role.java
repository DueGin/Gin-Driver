package duegin.ginDriver.domain.model;


import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 
 * @author DueGin
 * @TableName role
 */
@Data
@Accessors(chain = true)
public class Role implements Serializable {
    /**
     * 角色表id
     */
    private Long roleId;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 描述信息
     */
    private String description;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    /**
     * 状态
     */
    private String status;

    private static final long serialVersionUID = 1L;
}