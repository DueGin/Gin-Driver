package com.ginDriver.main.file.generator;

import java.util.Locale;

public class LocalFilePathGenerator implements IFilePathGenerator{
    @Override
    public String generate() {
        // 获取当前操作系统
        String osName = System.getProperties().get("os.name").toString().toLowerCase(Locale.ROOT);

        if (osName.startsWith("win")) {
            return "C:/";
        } else {
            return "/Users/duegin/Desktop/test/";
        }
    }
}
