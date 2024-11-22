package com.psm.domain.Model.model.repository.impl;

import com.psm.domain.Model.model.repository.Model3dRedis;
import com.psm.app.annotation.spring.Repository;
import com.psm.infrastructure.Cache.RedisCache;
import com.psm.infrastructure.Tus.Tus;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

@Repository
public class Model3dRedisImpl implements Model3dRedis {
    @Autowired
    Tus tus;

    @Autowired
    private RedisCache redisCache;
    public void addUploadModel(String id, String fullName)
    {
        Long expiration = tus.getExpirationPeriod();
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
