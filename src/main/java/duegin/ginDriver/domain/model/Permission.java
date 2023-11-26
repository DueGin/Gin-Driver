package duegin.ginDriver.domain.model;


import lombok.Data;

import java.io.Serializable;

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
    private Long permissionId;

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

    private static final long serialVersionUID = 1L;
}