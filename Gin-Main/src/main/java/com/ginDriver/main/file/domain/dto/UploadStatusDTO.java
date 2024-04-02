package com.ginDriver.main.file.domain.dto;

import com.ginDriver.main.domain.po.File;
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

    private File file;

    private String filePath;

    private String md5;

    public UploadStatusDTO(UploadStatus uploadStatus) {
        this.uploadStatus = uploadStatus;
    }

    public UploadStatusDTO(UploadStatus uploadStatus, String objectName, File file) {
        this.uploadStatus = uploadStatus;
        this.objectName = objectName;
        this.file = file;
    }

    public UploadStatusDTO(UploadStatus uploadStatus, String filePath, String md5) {
        this.uploadStatus = uploadStatus;
        this.filePath = filePath;
        this.md5 = md5;
    }
}
