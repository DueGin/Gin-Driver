package com.ginDriver.main.domain.po;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 字典表 实体类。
 *
 * @author DueGin
 * @since 1.0
 */
@Data
@Table(value = "sys_dict")
public class SysDict {

    @Id(keyType = KeyType.Auto)
    private Long id;

    /**
     * 字典类型
     */
    private String dictType;

    @Column(value = "label")
    private String label;

    @Column(value = "value")
    private Integer value;

    /**
     * 字典状态(0:禁用，1:启用)
     */
    @Column(value = "status")
    private Integer status;

    /**
     * 备注
     */
    @Column(value = "remark")
    private String remark;

    /**
     * 创建时间
     */
    @Column(value = "create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Column(value = "update_time")
    private LocalDateTime updateTime;


}
