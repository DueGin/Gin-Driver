package com.ginDriver.main.file.upload;

import com.ginDriver.main.file.constants.UploadHandleType;

import java.util.HashMap;
import java.util.Map;

public class FileUploadHandleFactory {
    private static final Map<UploadHandleType, FileUploadHandler> map;

    static {
        map = new HashMap<>();
//        map.put("Location", MyBeanFactory.getBean(LocalFileUploadHandler.class));
        map.put(UploadHandleType.LOCATION, new LocalFileUploadHandler());
    }



    /**
     * 通过type获取
     *
     * @param type {@link UploadHandleType}文件上传处理者
     * @return 指定类型的处理者
     */
    public static FileUploadHandler getFileUploadHandle(UploadHandleType type) {
        return map.get(type);
    }

}
