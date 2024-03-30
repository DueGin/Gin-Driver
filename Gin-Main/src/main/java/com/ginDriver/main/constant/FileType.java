package com.ginDriver.main.constant;

public enum FileType {
    other,
    media,
    system,
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