package com.ginDriver.main.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author DueGin
 */
@Component
@Data
public class FileProperties {

    @Value("${gin-driver.file.prefixPath:/media/}")
    private String filePrefixPath;

    public String getFilePrefixPath() {
        return filePrefixPath.endsWith("/") ? filePrefixPath : filePrefixPath + "/";
    }
}
