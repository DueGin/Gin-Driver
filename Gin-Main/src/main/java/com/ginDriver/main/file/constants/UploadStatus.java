package com.ginDriver.main.file.constants;

public enum UploadStatus {
    SUCCESS_END(201, "上传成功且为最后一个分片"),
    CHUNK_SUCCESS(202, "上传成功"),
    REPEAT(203, "文件重复"),
    SUCCESS_WHOLE(205, "没有分片文件上传成功"),
    FILE_CORRUPTION(406, "文件损坏"),
    FAIL(405, "上传失败"),
    NOT_FOUND_TOKEN(403, "未找到upload_token"),
    FILE_NOT_EXIST(404, "md5文件不存在，请修改exist值"),
    ERROR_UNKNOWN(500, "未知错误"),
    ERROR_FILE_CANNOT_IN_DB(501, "文件入库失败")
    ;


    private final int value;

    private final String explain;

    UploadStatus(int value, String explain) {
        this.value = value;
        this.explain = explain;
    }

    public int getValue() {
        return value;
    }

    public String getExplain() {
        return explain;
    }
}
