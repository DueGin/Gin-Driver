package com.ginDriver.main.file.constants;

import lombok.Getter;

/**
 * @author DueGin
 */
@Getter
public enum DownloadStatus {
    SUCCESS(2000, "下载成功"),

    ERROR_UNKNOWN(5000, "未知错误"),

    FILE_NOT_FOUND(4004, "不存在此文件");

    private final Integer code;

    private final String msg;

    DownloadStatus(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
