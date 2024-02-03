package com.ginDriver.main.domain.dto.place;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author DueGin
 */
@Data
public class PlaceDTO {
    @ExcelProperty("中文名")
    private String name;

    @ExcelProperty("adcode")
    private Integer adcode;

    @ExcelProperty("citycode")
    private Integer citycode;

    @ExcelIgnore
    private Integer cityAdcode;

    @ExcelIgnore
    private Integer provinceAdcode;
}
