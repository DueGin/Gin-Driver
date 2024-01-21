package com.ginDriver.main.domain.dto.sys.dict;

import com.ginDriver.main.domain.po.SysDictType;
import com.mybatisflex.core.paginate.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author DueGin
 */
@Data
@ApiModel("字典类型分页查询DTO")
public class SysDictTypePageDTO extends Page<SysDictType> {
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
     * 状态（0正常 1停用）
     */
    @ApiModelProperty(value = "状态（0正常 1停用）")
    private Integer status;
}
