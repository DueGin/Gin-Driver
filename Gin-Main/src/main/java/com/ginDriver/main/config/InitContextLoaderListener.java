package com.ginDriver.main.config;

import com.ginDriver.main.utils.GeoUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.Executor;

/**
 * 启动加载
 * @author DueGin
 */
@Component
public class InitContextLoaderListener implements InitializingBean {

    @Value("${amap.key:}")
    private String aMapKey;

    @Resource(name = "serviceExecutor")
    private Executor serviceExecutor;

    @Override
    public void afterPropertiesSet() {
        // 设置
        GeoUtil.aMapKey = aMapKey;
    }
}
