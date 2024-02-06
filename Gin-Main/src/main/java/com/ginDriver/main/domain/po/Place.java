package com.ginDriver.main.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 行政区域 实体类。
 *
 * @author DueGin
 * @since 1.0
 */
@Data
@ApiModel(value = "行政区域", description = "行政区域")
@TableName(value = "place")
public class Place {

    @ApiModelProperty(value = "")
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 区域名称
     */
    @ApiModelProperty(value = "区域名称")
    private String name;

    /**
     * 行政区域编码，唯一
     */
    @ApiModelProperty(value = "行政区域编码，唯一")
    private Integer adcode;

    /**
     * 县区所属的市区域编码
     */
    @ApiModelProperty(value = "县区所属的市区域编码")
    private Integer cityAdcode;

    /**
     * 县区或市所属省份区域编码
     */
    @ApiModelProperty(value = "县区或市所属省份区域编码")
    private Integer provinceAdcode;


}
