package com.hqs.flashsales.limit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;

/**
 * @author huangqingshi
 * @Date 2019-01-17
 */
@Slf4j
@Component
public class DistributedLimit {

    //注意RedisTemplate用的String,String，后续所有用到的key和value都是String的
    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @Resource
    RedisScript<Long> rateLimitScript;

    @Resource
    RedisScript<Long> limitScript;

    public Boolean distributedLimit(String key, String limit, String seconds) {
        Long id = 0L;

        try {
            id = redisTemplate.execute(limitScript, Collections.singletonList(key),
                    limit, seconds);
//            log.info("id:{}", id);
        } catch (Exception e) {
            log.error("error", e);
        }

        if(id == 0L) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean distributedRateLimit(String key, String limit, String seconds) {
        Long id = 0L;
        long intervalInMills = Long.valueOf(seconds) * 1000;
        long limitInLong = Long.valueOf(limit);
        long intervalPerPermit = intervalInMills / limitInLong;
//        Long refillTime = System.currentTimeMillis();
//        log.info("调用redis执行lua脚本, {} {} {} {} {}", "ratelimit", intervalPerPermit, refillTime,
//                limit, intervalInMills);
        try {
             id = redisTemplate.execute(rateLimitScript, Collections.singletonList(key),
                    String.valueOf(intervalPerPermit), String.valueOf(System.currentTimeMillis()),
                    String.valueOf(limitInLong), String.valueOf(intervalInMills));
        } catch (Exception e) {
            log.error("error", e);
        }

        if(id == 0L) {
            return false;
        } else {
            return true;
        }
    }

}
