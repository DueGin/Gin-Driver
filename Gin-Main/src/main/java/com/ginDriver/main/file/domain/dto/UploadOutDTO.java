package com.ginDriver.main.file.domain.dto;

import com.ginDriver.main.file.constants.UploadStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author DueGin
 */
@Data
@NoArgsConstructor
public class UploadOutDTO extends UploadStatusDTO{
    private String objectName;

    private Long fileId;

    public UploadOutDTO(UploadStatus uploadStatus, String objectName, Long fileId) {
        super(uploadStatus);
        this.objectName = objectName;
        this.fileId = fileId;
    }

}
