package com.ginDriver.common.verifyCode.service.impl;


import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.TimeUnit;


@Repository
public class IMemberVerifyCodeRepository {

    private final StringRedisTemplate redisTemplate;

    public IMemberVerifyCodeRepository(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String get(String key) {
        if (hasKey(key)) {
            ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();

            return valueOperations.get(key);
        }
        return null;

    }

    public void delete(String key) {
        if (hasKey(key)) {
            redisTemplate.delete(key);
        }
    }

    /**
     * 保存到redis 中
     * 码证码有效期 20分钟
     *
     * @param key             session id
     * @param verifyCodeDigit 码证码
     */
    public void save(String key, String verifyCodeDigit) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, verifyCodeDigit, 20L, TimeUnit.MINUTES);
    }

    public boolean hasKey(String key) {
        return Optional.ofNullable(redisTemplate.hasKey(key)).orElse(false);
    }
}
