package com.ginDriver.main.cache.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author DueGin
 */
@Component
public class BaseRedis {
    @Resource
    protected StringRedisTemplate stringRedisTemplate;

    @Resource
    protected RedisTemplate redisTemplate;
}
