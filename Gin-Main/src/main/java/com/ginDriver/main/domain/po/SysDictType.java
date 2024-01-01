package com.ginDriver.main.domain.po;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 实体类。
 *
 * @author DueGin
 * @since 1.0
 */
@Data
@ApiModel(value = "", description = "")
@Table(value = "sys_dict_type")
public class SysDictType {

    @ApiModelProperty(value = "")
    @Id(keyType = KeyType.Auto)
    private Long id;

    /**
     * 字典名称
     */
    @ApiModelProperty(value = "字典名称")
    @Column(value = "dict_name")
    private String dictName;

    /**
     * 状态（0正常 1停用）
     */
    @ApiModelProperty(value = "状态（0正常 1停用）")
    @Column(value = "status")
    private Integer status;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    @Column(value = "remark")
    private String remark;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @Column(value = "create_time")
    private java.time.LocalDateTime createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    @Column(value = "update_time")
    private java.time.LocalDateTime updateTime;


}
