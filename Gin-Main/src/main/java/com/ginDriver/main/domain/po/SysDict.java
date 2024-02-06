package com.ginDriver.main.domain.po;



import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 字典表 实体类。
 *
 * @author DueGin
 * @since 1.0
 */
@Data
@TableName(value = "sys_dict")
public class SysDict {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 字典类型
     */
    private String dictType;

    
    private String label;

    
    private Integer value;

    /**
     * 字典状态(0:禁用，1:启用)
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
