package com.ginDriver.main.file.domain.dto;

import com.ginDriver.main.file.constants.UploadStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author DueGin
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadStatusDTO {
    private UploadStatus uploadStatus;

    private String objectName;

    private Long fileId;

    private String filePath;

    private String md5;

    public UploadStatusDTO(UploadStatus uploadStatus) {
        this.uploadStatus = uploadStatus;
    }

    public UploadStatusDTO(UploadStatus uploadStatus, String objectName, Long fileId) {
        this.uploadStatus = uploadStatus;
        this.objectName = objectName;
        this.fileId = fileId;
    }

    public UploadStatusDTO(UploadStatus uploadStatus, String filePath, String md5) {
        this.uploadStatus = uploadStatus;
        this.filePath = filePath;
        this.md5 = md5;
    }
}
