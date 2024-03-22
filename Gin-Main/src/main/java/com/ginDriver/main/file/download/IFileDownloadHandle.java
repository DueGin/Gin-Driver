package com.ginDriver.main.file.download;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;

public interface IFileDownloadHandle {
    void download(File file, String userFileName, HttpServletRequest request, HttpServletResponse response);
}
