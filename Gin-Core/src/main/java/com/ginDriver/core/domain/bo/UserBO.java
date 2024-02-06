package com.ginDriver.core.domain.bo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author DueGin
 */
@Data
public class UserBO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String userAccount;

    private String username;

    /**
     * 头像文件名称
     */
    private String avatar;

    private String phone;

    private String email;

    private List<String> roleList;

    private Map<String, String> roleMap;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
