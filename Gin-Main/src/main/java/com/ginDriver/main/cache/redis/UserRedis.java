package com.ginDriver.main.cache.redis;

import com.ginDriver.core.domain.bo.UserBO;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author DueGin
 */
@Component
public class UserRedis extends BaseRedis {

    private static final String USER_PREFIX = "user:";


    private static String getUserBoKey(Long id) {
        return USER_PREFIX + id;
    }

    public void setUserBO(UserBO bo) {
        String userBoKey = getUserBoKey(bo.getId());
        redisTemplate.opsForValue().set(userBoKey, bo, 1, TimeUnit.DAYS);
    }

    public UserBO getUserBO(Long userId) {
        String userBoKey = getUserBoKey(userId);
        return (UserBO) redisTemplate.opsForValue().get(userBoKey);
    }

    public Boolean removeUserBO(Long userId){
        String userBoKey = getUserBoKey(userId);
        return redisTemplate.delete(userBoKey);
    }
}
