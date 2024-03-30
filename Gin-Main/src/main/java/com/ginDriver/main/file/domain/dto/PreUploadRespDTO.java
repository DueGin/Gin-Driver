package com.ginDriver.main.file.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author DueGin
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PreUploadRespDTO {
    private String md5;

    private String uploadId;

    private Integer exist;

    private String objectName;

    private String src;

    private Integer type;
}
