package com.ginDriver.main.domain.dto.sys.dict;

import com.ginDriver.main.domain.po.SysDict;
import com.mybatisflex.core.paginate.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author DueGin
 */
@Data
@ApiModel("字典分页DTO")
public class SysDictPageDTO extends Page<SysDict> {
    /**
     * 字典类型
     */
    @ApiModelProperty(value = "字典类型", required = true)
    private String dictType;
}
