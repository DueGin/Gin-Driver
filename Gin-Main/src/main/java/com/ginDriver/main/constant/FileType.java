package com.ginDriver.main.constant;

public enum FileType {
    other("other"),
    media("media"),
    system("system"),
    movie("movie");
    public final String name;

    FileType(String name) {
        this.name = name;
    }

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