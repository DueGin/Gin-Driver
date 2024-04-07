package com.ginDriver.main.file.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author DueGin
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PreUploadRespDTO {
    private String md5;

    private String contentType;

    private String uploadId;

    private Integer exist;

    private String objectName;

    private String src;

    private Integer fileType;

    /**
     * 未上传的分片（第几片）
     */
    private List<Integer> chunkList;
}
