package com.ginDriver.main.file.file_check;

import org.springframework.util.DigestUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileCheckMD5 implements IFileCheck {

    @Override
    public boolean fileCheck(String pathName, String internetMD5) {
        // 读取文件
        File file = new File(pathName);
        InputStream in = null;
        String md5 = null;
        try {
            in = new FileInputStream(file);
            // MD5加密
            md5 = DigestUtils.md5DigestAsHex(in);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 比对MD5值是否相同
        if (md5 == internetMD5) {
            return true;
        }
        return false;
    }
}
