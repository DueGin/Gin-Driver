package com.ginDriver.main.domain.dto.sys.dict;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ginDriver.main.domain.po.SysDict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * @author DueGin
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("字典分页DTO")
public class SysDictPageDTO extends Page<SysDict> {
    /**
     * 字典类型
     */
    @NotBlank(message = "字典类型不能为空")
    @ApiModelProperty(value = "字典类型", required = true)
    private String dictType;
}
