package com.ginDriver.main.file.upload;

import com.ginDriver.main.file.constants.UploadHandlerType;
import com.ginDriver.main.service.FileService;
import com.ginDriver.main.utils.MyBeanFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author DueGin
 */
public class FileUploadHandleFactory {
    private static final Map<UploadHandlerType, FileUploadHandler> map;

    static {
        FileService fileService = MyBeanFactory.getBean(FileService.class);
        map = new HashMap<>();
//        map.put("Location", MyBeanFactory.getBean(LocalFileUploadHandler.class));
        map.put(UploadHandlerType.LOCATION, new LocalFileUploadHandler());
    }


    /**
     * 通过type获取
     *
     * @param type {@link UploadHandlerType}文件上传处理者
     * @return 指定类型的处理者
     */
    public static FileUploadHandler getFileUploadHandle(UploadHandlerType type) {
        return map.get(type);
    }

}
