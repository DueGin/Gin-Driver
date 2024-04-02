package com.ginDriver.main.utils;

import cn.hutool.core.lang.Snowflake;

/**
 * @author DueGin
 */
public class IdUtil {

    public static long nextSnowflakeId(){
        Snowflake snowflake = new Snowflake();
        return snowflake.nextId();
    }
}
