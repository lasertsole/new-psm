package com.psm.infrastructure.Cache.decorator;

import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;

@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MultiLevelChannel {
    private RedissonClient redissonClient;
    private Caffeine caffeine;
}
