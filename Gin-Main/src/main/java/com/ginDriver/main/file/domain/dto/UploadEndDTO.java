package com.ginDriver.main.file.domain.dto;

import com.ginDriver.main.file.constants.UploadStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author DueGin
 */
@Data
@NoArgsConstructor
public class UploadEndDTO extends UploadOutDTO{

    private String filePath;

    private String md5;

    public UploadEndDTO(UploadStatus uploadStatus, String objectName, Long fileId, String filePath, String md5) {
        super(uploadStatus, objectName, fileId);
        this.filePath = filePath;
        this.md5 = md5;
    }
}
