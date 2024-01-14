package com.ginDriver.main.domain.dto.exif;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author DueGin
 */
@Data
public class ExifInfoDTO {

    private LocalDateTime createTime;

    private Long width;

    private Long height;

    private String mimeType;

    private String model;
}
