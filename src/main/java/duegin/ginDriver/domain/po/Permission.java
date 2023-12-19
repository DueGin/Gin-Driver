package duegin.ginDriver.domain.po;


import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 
 * @author DueGin
 * @TableName permission
 */
@Data
public class Permission implements Serializable {
    /**
     * 权限ID
     */
    private Long id;

    /**
     * 权限标识符
     */
    private String code;

    /**
     * 描述信息
     */
    private String description;

    /**
     * 请求地址
     */
    private String url;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    private static final long serialVersionUID = 1L;
}