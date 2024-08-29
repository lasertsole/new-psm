package com.psm.utils.ThirdAuth.github;

import com.psm.utils.Redis.RedisCache;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Getter
@Component
public class GithubAuthUtil {
    @Autowired
    private GithubAuthProperties githubAuthProperties;

    @Autowired
    private RedisCache redisCache;

    @Value("${spring.security.oauth2.expiration}")
    private Long expiration; //登录默认过期时间,单位为毫秒. 默认十分钟

    /**
     * 生成随机state字符串，这里可以存入Redis或者Set，返回时进行校验，不过要注意失效时间
     */
    public String genState(){
        String state = UUID.randomUUID().toString();
        //把完整信息存入redis
        redisCache.setCacheObject("githubState:"+state,1,Math.toIntExact(expiration / 1000 / 60), TimeUnit.MINUTES);
        return state;
    }

    /**
     * 校验state，防止CSRF
     * 校验成功后删除
     */
    public boolean checkState(String state){
        if(!Objects.isNull(redisCache.getCacheObject("githubState:"+state))){
            return true;
        }
        return false;
    }
}
