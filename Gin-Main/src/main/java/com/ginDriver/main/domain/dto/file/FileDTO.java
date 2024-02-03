package com.ginDriver.main.domain.dto.file;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author DueGin
 */
@Data
public class FileDTO {

    /**
     * 文件类型(桶名称)
     */
    @NotBlank
    @ApiModelProperty("文件类型(桶名称)")
    private String bucketName;

    /**
     * 文件名称
     */
    @NotBlank
    @ApiModelProperty("文件名称")
    private String fileName;
}
