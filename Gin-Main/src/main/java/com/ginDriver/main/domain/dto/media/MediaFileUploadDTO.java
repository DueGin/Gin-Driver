package com.ginDriver.main.domain.dto.media;

import com.ginDriver.main.file.domain.dto.ChunkDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

/**
 * @author DueGin
 */
@Data
public class MediaFileUploadDTO extends ChunkDTO {

    /**
     * 媒体拍摄时间
     */
    @ApiModelProperty("媒体拍摄时间")
    private LocalDateTime originalDateTime;

    /**
     * 媒体宽度
     */
    @ApiModelProperty("媒体宽度")
    private Integer width;

    /**
     * 媒体高度
     */
    @ApiModelProperty("媒体高度")
    private Integer height;

    /**
     * 媒体类型
     */
    @ApiModelProperty("媒体类型")
    private String mimeType;

    /**
     * 拍摄设备
     */
    @ApiModelProperty("拍摄设备")
    private String model;

    /**
     * 经度
     */
    @ApiModelProperty("经度")
    private String longitude;

    /**
     * 纬度
     */
    @ApiModelProperty("纬度")
    private String latitude;

    private MultipartFile file;
}