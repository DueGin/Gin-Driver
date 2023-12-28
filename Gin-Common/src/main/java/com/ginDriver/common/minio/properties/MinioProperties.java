package com.ginDriver.common.minio.properties;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author DueGin
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "minio")
public class MinioProperties {
    @ApiModelProperty("endPoint是一个URL，域名，IPv4或者IPv6地址")
    private String endpoint;

    @ApiModelProperty("accessKey类似于用户ID，用于唯一标识你的账户")
    private String accessKey;

    @ApiModelProperty("secretKey是你账户的密码")
    private String secretKey;

    @ApiModelProperty("如果是true，则用的是https而不是http,默认值是true")
    private Boolean secure;

    @ApiModelProperty("默认存储桶")
    private String bucketName;

    @ApiModelProperty("配置目录")
    private String configDir;

    /**
     * URL过期时间
     */
    @Value("${minio.expiry:}")
    private Integer expiry;
}
