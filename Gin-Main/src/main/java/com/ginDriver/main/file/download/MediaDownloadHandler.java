package com.ginDriver.main.file.download;

import com.ginDriver.main.domain.po.Md5File;
import com.ginDriver.main.service.FileService;
import com.ginDriver.main.service.Md5FileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @author DueGin
 */
@Slf4j
@Component
public class MediaDownloadHandler {

    private final static String UTF8 = "utf-8";

    @Resource
    private FileService fileService;

    @Resource
    private Md5FileService md5FileService;

    public void download(String basePath, Long fileId, HttpServletRequest request, HttpServletResponse response) {

        com.ginDriver.main.domain.po.File f = fileService.getById(fileId);
        if (f == null) {
            return;
        }
        Md5File md5File = md5FileService.getById(f.getMd5FileId());

        // 1.读取需要下载的文件
        InputStream is = null;
        OutputStream os = null;
        try {
            // 打开文件流
            try {
                String ext = f.getName().substring(f.getName().lastIndexOf("."));
                File file = new File(basePath + md5File.getMd5() + ext);
                is = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            // 2.设置下载响应头
            response.setContentType(md5File.getContentType());
            // 2.1获取文件名
//            String fileName = file.getName();

            // 2.2获取浏览器标识 -- 表明是要传回给什么类型浏览器（谷歌、IE、火狐）
            String userAgent = request.getHeader("user-agent");
            log.debug("当前请求的浏览器类型为: " + userAgent);
            if (userAgent.contains("Firefox")) {
                //旧版本的火狐使用是base64格式，如果传中文会乱码
                response.setHeader("Content-Disposition", "attachment; filename==?UTF-8?B?" + URLEncoder.encode(f.getName(), StandardCharsets.UTF_8) + "?=");
            } else {
                //谷歌 Edge 都能解析 UTF-8 格式
                //attachment： -- 表示附件，表示下载使用
                //filename： -- 表示下载时的文件名
                //URLEncoder.encode -- 指定编码格式，不指定中文名字会乱码
                response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(f.getName(), StandardCharsets.UTF_8));
            }

            // 3.把下载的文件内容回传给客户端
            os = response.getOutputStream();
            //流拷贝: 读取输入流中的所有内容进行回传
            assert is != null;
            IOUtils.copy(is, os);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
