package com.ginDriver.main.domain.po;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
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
@Table(value = "place")
public class Place {

    @ApiModelProperty(value = "")
    @Id(keyType = KeyType.Auto)
    private Integer id;

    /**
     * 区域名称
     */
    @ApiModelProperty(value = "区域名称")
    @Column(value = "name")
    private String name;

    /**
     * 行政区域编码，唯一
     */
    @ApiModelProperty(value = "行政区域编码，唯一")
    @Column(value = "adcode")
    private Integer adcode;

    /**
     * 县区所属的市区域编码
     */
    @ApiModelProperty(value = "县区所属的市区域编码")
    @Column(value = "city_adcode")
    private Integer cityAdcode;

    /**
     * 县区或市所属省份区域编码
     */
    @ApiModelProperty(value = "县区或市所属省份区域编码")
    @Column(value = "province_adcode")
    private Integer provinceAdcode;


}
