package com.psm.domain.Model.repository.impl;

import com.psm.domain.Model.repository.ModelRedis;
import com.psm.infrastructure.annotation.spring.Repository;
import com.psm.infrastructure.utils.Redis.RedisCache;
import com.psm.infrastructure.utils.Tus.TusUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

@Repository
public class ModelRedisImpl implements ModelRedis {
    @Autowired
    TusUtil tusUtil;

    @Autowired
    private RedisCache redisCache;
    public void addUploadModel(String id, String fullName)
    {
        Long expiration = tusUtil.getExpirationPeriod();
        redisCache.setCacheObject("uploadModel:"+id, fullName, Math.toIntExact(expiration / 1000), TimeUnit.SECONDS);
    }

    public String getUploadModel(String id)
    {
        return redisCache.getCacheObject("uploadModel:"+id);
    }

    public void removeUploadModel(String id)
    {
        redisCache.deleteObject("uploadModel:"+id);
    }
}
