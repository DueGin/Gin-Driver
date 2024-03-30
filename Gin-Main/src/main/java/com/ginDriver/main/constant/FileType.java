package com.ginDriver.main.constant;

/**
 * 文件类型
 *
 * @author DueGin
 */
public enum FileType {
    other,
    system,
    media,
    movie;


    public static FileType getFileTypeByIdx(Integer fileTypeIdx) {
        if (fileTypeIdx == null) {
            return null;
        }
        FileType[] fileTypes = FileType.values();
        if (fileTypeIdx < 0 || fileTypeIdx >= fileTypes.length) {
            return null;
        }
        return fileTypes[fileTypeIdx];
    }
}