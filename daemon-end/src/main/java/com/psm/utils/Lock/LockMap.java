package com.psm.utils.Lock;

import com.psm.infrastructure.Redis.utils.RedisConstants;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockMap {
    private static final Map<String, Lock> LOCK_MAP = new HashMap<>();

    private LockMap() {
    }

    /**
     * 使用双重检查·单例模式为每个实体类创建一个表锁
     *
     * @param entityClass 实体类Class对象
     * @return 本地锁实例
     */
    public static Lock getLocalLock(Class<?> entityClass) {
        Objects.requireNonNull(entityClass);
        return getLocalLock(entityClass.getName());
    }

    /**
     * 使用双重检查·单例模式为每个实体类创建一个表锁
     *
     * @param className 实体类名称
     * @return 本地锁实例
     */
    public static Lock getLocalLock(String className) {
        Objects.requireNonNull(className);
        String key = RedisConstants.createLockKey(className);
        if (LOCK_MAP.get(key) == null) {
            synchronized (LockMap.class) {
                if (LOCK_MAP.get(key) == null) {
                    // 创建一个可重入锁
                    LOCK_MAP.put(key, new ReentrantLock());
                }
            }
        }
        return LOCK_MAP.get(key);
    }

    /**
     * 使用双重检查·单例模式为每个实体类创建一个表锁
     *
     * @param clazz 实体类名称
     * @return 本地锁实例
     */
    public static Lock getLocalLock(Class<?> clazz, Serializable id) {
        Objects.requireNonNull(clazz);
        String key = RedisConstants.createLockKey(clazz, id);
        if (LOCK_MAP.get(key) == null) {
            synchronized (LockMap.class) {
                if (LOCK_MAP.get(key) == null) {
                    // 创建一个可重入锁
                    LOCK_MAP.put(key, new ReentrantLock());
                }
            }
        }
        return LOCK_MAP.get(key);
    }
}
