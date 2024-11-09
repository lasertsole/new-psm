package com.psm.utils.Lock;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.function.Supplier;

@Slf4j
public class LockUtils {
    private LockUtils() {
    }


    /**
     * 通过封装减少try-catch冗余代码
     *
     * @param meta     {@link LockMeta}锁元数据子类实例
     * @param supplier 回调方法
     * @param <R>      返回值类型
     */
    public static <R> R tryLock(LockMeta meta, Supplier<R> supplier) {
        Objects.requireNonNull(supplier);
        RLock lock = Objects.requireNonNull(meta.getLock());
        try {
            if (lock.tryLock(meta.getWaitTime(), meta.getLeaseTime(), meta.getTimeUnit())) {
                // 回调被锁业务逻辑
                return supplier.get();
            } else {
                long sec = TimeUnit.SECONDS.convert(meta.getWaitTime(), meta.getTimeUnit());
                log.error("等待{}秒仍未获得锁，已超时，请重新申请锁", sec);
                throw new RuntimeException("获取锁等待超时");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            Optional.of(lock).ifPresent(Lock::unlock);
        }
    }

    /**
     * 通过封装减少try-catch冗余代码
     *
     * @param meta     {@link LockMeta}锁元数据子类实例
     * @param supplier 回调方法
     * @param <R>      返回值类型
     */
    public static <R> R tryLock(JvmLockMeta meta, Supplier<R> supplier) {
        Objects.requireNonNull(supplier);
        Lock lock = Objects.requireNonNull(meta.getLock());
        try {
            if (lock.tryLock(meta.getWaitTime(), meta.getTimeUnit())) {
                // 回调被锁业务逻辑
                return supplier.get();
            } else {
                throw new RuntimeException("获取锁等待超时");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            Optional.of(lock).ifPresent(Lock::unlock);
        }
    }
}
