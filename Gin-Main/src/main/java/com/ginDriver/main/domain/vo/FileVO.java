package com.ginDriver.main.domain.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author DueGin
 */
@Data
@Accessors(chain = true)
public class FileVO {

    private String fileName;

    private String url;

    private Long fileId;
}
