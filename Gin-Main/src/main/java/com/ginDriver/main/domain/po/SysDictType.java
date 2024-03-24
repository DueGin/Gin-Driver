package com.ginDriver.main.domain.po;



import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 实体类。
 *
 * @author DueGin
 * @since 1.0
 */
@Data
@ApiModel(value = "", description = "")
@TableName(value = "sys_dict_type")
public class SysDictType {


    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 字典类型编码
     */
    @ApiModelProperty("字典类型编码")
    private String code;

    /**
     * 字典名称
     */
    @ApiModelProperty(value = "字典名称")
    private String name;

    /**
     * 状态（1正常 0停用）
     */
    @ApiModelProperty(value = "状态（1正常 0停用）")
    private Integer status;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;


}
