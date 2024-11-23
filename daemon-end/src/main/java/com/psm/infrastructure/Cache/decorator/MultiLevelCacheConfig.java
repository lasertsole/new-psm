package com.psm.infrastructure.Cache.decorator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MultiLevelCacheConfig {
    private long TTL;
    private long maxIdleTime;
    private int maxSize;
}
