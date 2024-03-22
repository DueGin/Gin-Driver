package com.ginDriver.main.file.utils;


import org.springframework.util.DigestUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class EncoderUtil {
    public static String fileToMd5(File file) throws IOException {
        try (InputStream is = new FileInputStream(file)) {
            return DigestUtils.md5DigestAsHex(is);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
