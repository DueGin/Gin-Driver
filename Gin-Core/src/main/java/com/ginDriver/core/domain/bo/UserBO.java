package com.ginDriver.core.domain.bo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author DueGin
 */
@Data
public class UserBO {

    private Long id;

    private String username;

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
