package com.ginDriver.main.file.constants;

import lombok.Getter;

/**
 * 上传文件状态
 *
 * @author DueGin
 */
@Getter
public enum UploadStatus {
    SUCCESS_END(2000, "上传成功"),
    SUCCESS_CHUNK(2002, "上传分片成功"),
    //    REPEAT(2003, "文件重复"),
//    SUCCESS_WHOLE(2005, "没有分片文件上传成功"),
    FILE_CORRUPTION(4006, "文件损坏"),
    FAIL(4005, "上传失败"),
    FAIL_NOT_FOUND_BODY(4040, "文件请求体不存在"),
    NOT_FOUND_TOKEN(4003, "未找到upload_id"),
    FILE_NOT_EXIST(4041, "md5文件不存在，请修改exist值"),
    ERROR_UNKNOWN(5000, "未知错误"),
    ERROR_FILE_CANNOT_IN_DB(5001, "文件入库失败");


    private final int value;

    private final String msg;

    UploadStatus(int value, String msg) {
        this.value = value;
        this.msg = msg;
    }

}
