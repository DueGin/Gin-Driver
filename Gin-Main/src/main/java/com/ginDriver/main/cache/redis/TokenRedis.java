package com.ginDriver.main.cache.redis;

import com.ginDriver.common.utils.JwtTokenUtils;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author DueGin
 */
@Component
public class TokenRedis extends BaseRedis{

    private static final String BLANK_TOKEN_PREFIX = "token:blank:";


    private static String getBlankTokenKey(String token){
        return BLANK_TOKEN_PREFIX + token;
    }

    public void setBlankToken(String token){
        String blankTokenKey = getBlankTokenKey(token);
        redisTemplate.opsForValue().set(blankTokenKey, "1", JwtTokenUtils.EXPIRATION_REMEMBER, TimeUnit.SECONDS);
    }

    public boolean hasBlankToken(String token){
        String blankTokenKey = getBlankTokenKey(token);
        return Boolean.TRUE.equals(redisTemplate.hasKey(blankTokenKey));
    }
}
