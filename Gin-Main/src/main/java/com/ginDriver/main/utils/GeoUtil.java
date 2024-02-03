package com.ginDriver.main.utils;

import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.ginDriver.common.utils.http.HttpRequestUtil;
import com.ginDriver.core.exception.ApiException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author DueGin
 */
public class GeoUtil {
    private static Logger log = LoggerFactory.getLogger(GeoUtil.class);

    public static String aMapKey = "";

    public static JSONObject getAddressWithLngLat(String lng, String lat) {
        if(StringUtils.isBlank(aMapKey)){
            log.error("请先设置amap key");
            throw new ApiException("请先设置amap key");
        }

        Map<String, String> map = MapUtil.<String, String>builder()
                .put("output", "json")
                .put("location", lng + "," + lat)
                .put("key", aMapKey)
                .build();

        String respBody = HttpRequestUtil.sendGet("https://restapi.amap.com/v3/geocode/regeo", map, null);
        return JSON.parseObject(respBody);
    }
}
