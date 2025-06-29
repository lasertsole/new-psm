package com.psm.infrastructure.DB.utils;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.psm.infrastructure.Cache.utils.RedisConstants;
import com.psm.infrastructure.Cache.utils.RedisUtils;
import com.psm.utils.Boolean.BooleanUtils;
import com.psm.utils.Collect.ColUtils;
import com.psm.utils.Entity.EntityUtils;
import com.psm.utils.Lock.JvmLockMeta;
import com.psm.utils.Lock.LockMeta;
import com.psm.utils.Lock.LockUtils;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.function.Function;

@Slf4j
public class PlusUtils {
    /**
     * <p>增强MybatisPlus主键查询方法{@link com.baomidou.mybatisplus.core.mapper.BaseMapper#selectById(Serializable)}</p>
     * <p>增强MybatisPlus主键查询方法{@link com.baomidou.mybatisplus.extension.service.IService#getById(Serializable)}</p>
     * <p>使其具备Redis数据缓存功能 提高数据访问效率</p>
     * 普通无缓存查询数据库
     * <pre>
     *     User user = baseMapper.selectById(pkVal);
     *     User user = service.getById(pkVal);
     * </pre>
     * 使用缓存查询数据库
     * <pre>
     *     User user = PlusUtils.ifNotCache(pkVal, User.class, baseMapper::selectById);
     *     User user = PlusUtils.ifNotCache(pkVal, User.class, service::getById);
     * </pre>
     *
     * @param id     主键查询ID
     * @param clazz  DO实体类Class对象
     * @param action 访问数据库的回调
     * @param <E>    主键ID的数据类型
     * @param <T>    DO实体类数据类型
     * @return DO实体类实例
     * @deprecated 无锁实现 存在缓存击穿问题
     */
    public static <E extends Serializable, T> T ifNotCache(E id, Class<T> clazz, Function<E, T> action) {
        String key = RedisConstants.createCacheKey(clazz, id);
        T value = RedisUtils.getObject(key, clazz);
        if (value == null) {
            T newValue = action.apply(id);
            Optional.ofNullable(newValue).ifPresent(e -> RedisUtils.save(key, e, 0, TimeUnit.SECONDS));
            return newValue;
        }
        return value;
    }

    /**
     * <p>增强MybatisPlus主键查询方法{@link com.baomidou.mybatisplus.core.mapper.BaseMapper#selectById(Serializable)}</p>
     * <p>增强MybatisPlus主键查询方法{@link com.baomidou.mybatisplus.extension.service.IService#getById(Serializable)}</p>
     * <p>使其具备Redis数据缓存功能 提高数据访问效率</p>
     * 普通无缓存查询数据库
     * <pre>
     *     User user = baseMapper.selectById(pkVal);
     *     User user = service.getById(pkVal);
     * </pre>
     * 使用缓存查询数据库
     * <pre>
     *     User user = PlusUtils.ifNotCache(pkVal, User.class, baseMapper::selectById);
     *     User user = PlusUtils.ifNotCache(pkVal, User.class, service::getById);
     * </pre>
     *
     * @param id     主键查询ID
     * @param clazz  DO实体类Class对象
     * @param action 访问数据库的回调
     * @param <E>    主键ID的数据类型
     * @param <T>    DO实体类数据类型
     * @return DO实体类实例
     */
    public static <E extends Serializable, T> T ifNotCache(E id, Class<T> clazz, Function<E, T> action, Lock lock) {
        T value = RedisUtils.getObject(id, clazz);
        String key = RedisConstants.createCacheKey(clazz, id);
        if (value == null) {
            T newValue = doVisitDb(id, -1L, clazz, action, lock);
            Optional.ofNullable(newValue).ifPresent(e -> {
                RedisUtils.save(key, e, 0, TimeUnit.SECONDS);
            });
            return newValue;
        }
        log.debug("缓存已命中，从Redis取值返回，跳过访问数据库环节，其中Key:{} Value:{}", key, value);
        return value;
    }

    /**
     * <p>增强MybatisPlus主键查询方法{@link com.baomidou.mybatisplus.core.mapper.BaseMapper#selectById(Serializable)}</p>
     * <p>增强MybatisPlus主键查询方法{@link com.baomidou.mybatisplus.extension.service.IService#getById(Serializable)}</p>
     * <p>使其具备Redis数据缓存功能 提高数据访问效率</p>
     * 普通无缓存查询数据库
     * <pre>
     *     User user = baseMapper.selectById(pkVal);
     *     User user = service.getById(pkVal);
     * </pre>
     * 使用缓存查询数据库
     * <pre>
     *     User user = PlusUtils.ifNotCache(pkVal, User.class, baseMapper::selectById);
     *     User user = PlusUtils.ifNotCache(pkVal, User.class, service::getById);
     * </pre>
     *
     * @param id     主键查询ID
     * @param clazz  DO实体类Class对象
     * @param ms     过期时间 毫秒
     * @param action 访问数据库的回调
     * @param <E>    主键ID的数据类型
     * @param <T>    DO实体类数据类型
     * @return DO实体类实例
     * @deprecated 无锁实现 存在缓存击穿问题
     */
    public static <E extends Serializable, T> T ifNotCache(E id, Class<T> clazz, long ms, Function<E, T> action) {
        T value = RedisUtils.getObject(id, clazz);
        if (value == null) {
            T newValue = action.apply(id);
            Optional.ofNullable(newValue).ifPresent(e -> {
                RedisUtils.save(id, e, ms, TimeUnit.MILLISECONDS);
                log.debug("缓存未命中，已向Redis添加缓存：{}", e);
            });
            return newValue;
        }
        return value;
    }

    /**
     * <p>增强MybatisPlus主键查询方法{@link com.baomidou.mybatisplus.core.mapper.BaseMapper#selectById(Serializable)}</p>
     * <p>增强MybatisPlus主键查询方法{@link com.baomidou.mybatisplus.extension.service.IService#getById(Serializable)}</p>
     * <p>使其具备Redis数据缓存功能 提高数据访问效率</p>
     * 普通无缓存查询数据库
     * <pre>
     *     User user = baseMapper.selectById(pkVal);
     *     User user = service.getById(pkVal);
     * </pre>
     * 使用缓存查询数据库
     * <pre>
     *     User user = PlusUtils.ifNotCache(pkVal, User.class, baseMapper::selectById);
     *     User user = PlusUtils.ifNotCache(pkVal, User.class, service::getById);
     * </pre>
     *
     * @param id     主键查询ID
     * @param clazz  DO实体类Class对象
     * @param ms     过期时间 毫秒
     * @param action 访问数据库的回调
     * @param <E>    主键ID的数据类型
     * @param <T>    DO实体类数据类型
     * @return DO实体类实例
     */
    public static <E extends Serializable, T> T ifNotCache(E id, Class<T> clazz, long ms, Function<E, T> action, Lock lock) {
        T value = RedisUtils.getObject(id, clazz);
        String key = RedisConstants.createCacheKey(clazz, id);
        if (value == null) {
            T newValue = doVisitDb(id, ms, clazz, action, lock);
            Optional.ofNullable(newValue).ifPresent(e -> {
                RedisUtils.save(id, e, ms, TimeUnit.MILLISECONDS);
            });
            return newValue;
        }
        log.debug("缓存已命中，从Redis取值返回，跳过访问数据库环节，其中Key:{} Value:{}", key, value);
        return value;
    }

    /**
     * <p>增强MybatisPlus主键查询方法{@link com.baomidou.mybatisplus.core.mapper.BaseMapper#selectById(Serializable)}</p>
     * <p>增强MybatisPlus主键查询方法{@link com.baomidou.mybatisplus.extension.service.IService#getById(Serializable)}</p>
     * <p>使其具备Redis数据缓存功能 提高数据访问效率</p>
     * 普通无缓存查询数据库
     * <pre>
     *     User user = baseMapper.selectById(pkVal);
     *     User user = service.getById(pkVal);
     * </pre>
     * 使用无锁缓存查询数据库
     * <pre>
     *     User user = PlusUtils.ifNotCache(pkVal, User.class, baseMapper::selectById);
     *     User user = PlusUtils.ifNotCache(pkVal, User.class, service::getById);
     * </pre>
     * 使用有锁缓存查询数据库 解决缓存穿透问题
     * <pre>
     *     User user = PlusUtils.ifNotCache(pkVal, User.class, 10000, service::getById, redisson);
     * </pre>
     *
     * @param id       主键查询ID
     * @param clazz    DO实体类Class对象
     * @param ms       过期时间 毫秒
     * @param action   访问数据库的回调
     * @param redisson {@link RedissonClient}实例
     * @param <E>      主键ID的数据类型
     * @param <T>      DO实体类数据类型
     * @return DO实体类实例
     */
    public static <E extends Serializable, T> T ifNotCache(E id, Class<T> clazz, long ms, Function<E, T> action, RedissonClient redisson) {
        T value = RedisUtils.getObject(id, clazz);
        if (value == null) {
            return doVisitDb(id, ms, clazz, action, redisson);
        }
        return value;
    }


    /**
     * <p>增强MybatisPlus主键查询方法{@link com.baomidou.mybatisplus.core.mapper.BaseMapper#selectBatchIds(Collection)}</p>
     * <p>增强MybatisPlus主键查询方法{@link com.baomidou.mybatisplus.extension.service.IService#listByIds(Collection)}</p>
     * <p>使其具备Redis数据缓存功能 提高数据访问效率</p>
     * 普通无缓存查询数据库
     * <pre>
     *     User[] users = baseMapper.selectBatchIds(ids);
     *     User[] users = service.listByIds(ids);
     * </pre>
     * 使用缓存查询数据库
     * <pre>
     *     User[] users = PlusUtils.ifNotCache(ids, User.class, baseMapper::selectBatchIds);
     *     User[] users = PlusUtils.ifNotCache(ids, User.class, service::listByIds);
     * </pre>
     *
     * @param ids    批主键查询ID
     * @param clazz  DO实体类Class对象
     * @param ms     过期时间 毫秒 当参数小于0代表不过期
     * @param action 访问数据库的回调
     * @param <T>    DO实体类数据类型
     * @return DO实体类实例
     */
    public static <T> List<T> ifNotCache(Collection<? extends Serializable> ids, Class<T> clazz, long ms, Function<Collection<? extends Serializable>, ? extends List<T>> action) {
        List<String> keys = RedisConstants.createCacheKey(clazz, ids);
        List<T> redisResults = RedisUtils.multiGet(keys, clazz);
        if (redisResults.size() < ids.size()) {
            TableInfo tableInfo = TableInfoHelper.getTableInfo(clazz);
            if (tableInfo == null) {
                log.error("实体类{}未被MybatisPlus管理", clazz.getName());
                throw new RuntimeException("当前实体类不属于DO层");
            }
            List<? extends Serializable> remainIds = getRemainIds(ids, redisResults, tableInfo);
            if (!remainIds.isEmpty()) {
                List<T> newValue = action.apply(remainIds);
                if (!newValue.isEmpty()) {
                    Map<String, T> map = EntityUtils.toMap(newValue, e -> RedisConstants.createCacheKey(clazz, (String) pkVal(tableInfo, e)), Function.identity());
                    RedisUtils.multiSet(map, clazz, 0, TimeUnit.SECONDS);
                    redisResults.addAll(newValue);
                    if (ms > 0) {
                        map.forEach((k, v) -> RedisUtils.expire(k, ms, TimeUnit.MILLISECONDS));
                    }
                }
            }
        }
        return redisResults;
    }

    /**
     * <p>有缓存主键更新数据 重载{@link com.baomidou.mybatisplus.extension.service.IService#updateById(Object)}方法</p>
     *
     * @param entity 实体类对象
     * @param clazz  实体类Class对象
     * @param action 更新操作回调
     * @param <T>    实体类泛型
     * @return 更新操作状态
     */
    public static <T> boolean updateById(T entity, Class<T> clazz, Function<T, Boolean> action) {
        Serializable id = pkVal(entity, clazz);
        String key = RedisConstants.createCacheKey(clazz, id);
        RedisUtils.remove(key);
        boolean result = action.apply(entity);
        RedisUtils.remove(key);
        return result;
    }

    /**
     * <p>有缓存主键更新数据 重载{@link com.baomidou.mybatisplus.extension.service.IService#updateById(Object)}方法</p>
     *
     * @param entity       实体类对象
     * @param clazz        实体类Class对象
     * @param updateAction 更新操作回调
     * @param <T>          实体类泛型
     * @return 更新操作状态
     */
    public static <T> boolean updateById(T entity, Class<T> clazz, Function<T, Boolean> updateAction, Function<Serializable, T> selectAction) {
        Serializable id = pkVal(entity, clazz);
        boolean result = updateAction.apply(entity);
        BooleanUtils.ifTrue(result, () -> RedisUtils.save(id, selectAction.apply(id), 0, TimeUnit.SECONDS));
        return result;
    }

    /**
     * <p>有缓存主键更新数据 重载{@link com.baomidou.mybatisplus.extension.service.IService#updateById(Object)}方法</p>
     *
     * @param entity       实体类对象
     * @param clazz        实体类Class对象
     * @param updateAction 更新操作回调
     * @param <T>          实体类泛型
     * @return 更新操作状态
     */
    public static <T> boolean saveOrUpdate(T entity, Class<T> clazz, Function<T, Boolean> updateAction) {
        Serializable id = pkVal(entity, clazz);
        boolean result = updateAction.apply(entity);
        if (result) {
            String key = RedisConstants.createCacheKey(clazz, id);
            if (RedisUtils.remove(key)) {
                log.debug("[数据库执行更新操作，同时将Redis中主键ID:{}对应的缓存数据删除，其中Key:{}]", id, key);
            }
        }
        return result;
    }

    public static <T> boolean saveOrUpdateBatch(Collection<T> entityList, Class<T> clazz, Function<Collection<T>, Boolean> updateAction) {
        List<Serializable> ids = EntityUtils.toList(entityList, e -> pkVal(e, clazz));
        boolean result = updateAction.apply(entityList);
        if (result) {
            List<String> keys = EntityUtils.toList(ids, e -> RedisConstants.createCacheKey(clazz, e));
            if (RedisUtils.remove(keys) > 0) {
                log.debug("[数据库执行更新操作，同时将Redis中主键ID:{}对应的缓存数据删除，其中Key:{}]", ids.toArray(), keys.toArray());
            }
        }
        return result;
    }


    /**
     * <p>有缓存主键更新数据 重载{@link com.baomidou.mybatisplus.extension.service.IService#updateById(Object)}方法</p>
     *
     * @param entity       实体类对象
     * @param clazz        实体类Class对象
     * @param updateAction 更新操作回调
     * @param selectAction 主键查询操作回调
     * @param <T>          实体类泛型
     * @return 更新操作状态
     */
    public static <T> boolean updateById(T entity, Class<T> clazz, Function<T, Boolean> updateAction, Function<Serializable, T> selectAction, Lock lock) {
        // 获取实体类主键值
        Serializable id = pkVal(entity, clazz);
        JvmLockMeta meta = JvmLockMeta.of(lock, 3, TimeUnit.SECONDS);
        return LockUtils.tryLock(meta, () -> {
            boolean result = updateAction.apply(entity);
            BooleanUtils.ifTrue(result, () -> RedisUtils.save(id, selectAction.apply(id), 0, TimeUnit.SECONDS));
            return result;
        });

    }

    public static <T> boolean updateById(T entity, Class<T> clazz, Function<T, Boolean> action, RedissonClient redisson) {
        Objects.requireNonNull(redisson);
        Serializable id = pkVal(entity, clazz);
        String cacheKey = RedisConstants.createCacheKey(clazz, id);
        String lockKey = RedisConstants.createLockKey(clazz, id);
        RLock lock = redisson.getLock(lockKey);
        try {
            if (lock.tryLock(5, 10, TimeUnit.SECONDS)) {
                RedisUtils.remove(cacheKey);
                boolean result = action.apply(entity);
                RedisUtils.remove(cacheKey);
                return result;
            }
            return false;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            Optional.ofNullable(lock).ifPresent(RLock::unlock);
        }
    }

    /**
     * <p>有缓存主键删除数据</p>
     */
    public static <T> boolean removeById(Serializable id, Class<T> clazz, Function<Serializable, Boolean> action) {
        String key = RedisConstants.createCacheKey(clazz, id);
        boolean result = action.apply(id);
        if (result) {
            RedisUtils.remove(key);
            log.debug("[DO实体类{}映射数据库表的主键ID为{}的记录已删除，同时在Redis中删除了该条缓存，其中Key:{}]", clazz.getSimpleName(), id, key);
        }
        return result;
    }

    /**
     * <p>有缓存主键删除</p>
     */
    public static <T> boolean removeById(T entity, Class<T> clazz, Function<T, Boolean> action) {
        Serializable id = pkVal(entity, clazz);
        String key = RedisConstants.createCacheKey(clazz, id);
        boolean result = action.apply(entity);
        if (result) {
            RedisUtils.remove(key);
            log.debug("[DO实体类{}映射数据库表的主键ID为{}的记录已删除，同时在Redis中删除了该条缓存，其中Key:{}]", clazz.getSimpleName(), id, key);
        }
        return result;
    }

    /**
     * <p>有缓存批量删除</p>
     */
    public static <T> boolean removeByIds(Collection<? extends Serializable> idList, Class<T> clazz, Function<Collection<String>, Boolean> action) {
        Set<String> keys = EntityUtils.toSet(idList, e -> RedisConstants.createCacheKey(clazz, e));
        boolean result = action.apply(keys);
        if (result) {
            RedisUtils.remove(keys);
            log.debug("[DO实体类{}映射数据库表的主键ID为{}的记录已删除，同时在Redis中删除了该条缓存，其中Key:{}]", clazz.getSimpleName(), idList, keys);
        }
        return result;
    }


    /**
     * 具体查询数据库逻辑
     */
    private static <E extends Serializable, T> T doVisitDb(E id, long ms, Class<T> clazz, Function<E, T> action, RedissonClient redisson) {
        Objects.requireNonNull(redisson);
        String cacheKey = RedisConstants.createCacheKey(clazz, id);
        String lockKey = RedisConstants.createLockKey(clazz, id);
        RLock lock = redisson.getLock(lockKey);
        LockMeta meta = LockMeta.of(lock, 5, 10, TimeUnit.SECONDS);
        return LockUtils.tryLock(meta, () -> noLockVisitDb(id, ms, clazz, action, cacheKey));
    }


    /**
     * 具体查询数据库逻辑
     */
    private static <E extends Serializable, T> T doVisitDb(E id, long ms, Class<T> clazz, Function<E, T> action, Lock lock) {
        String cacheKey = RedisConstants.createCacheKey(clazz, id);
        JvmLockMeta meta = JvmLockMeta.of(lock, 5, TimeUnit.SECONDS);
        return LockUtils.tryLock(meta, () -> noLockVisitDb(id, ms, clazz, action, cacheKey));
    }

    private static <E extends Serializable, T> T noLockVisitDb(E id, long ms, Class<T> clazz, Function<E, T> action, String cacheKey) {
        // 再次检查一下Redis里面有没有数据 有的话直接返回 没有再取查询DB
        T value = RedisUtils.getObject(cacheKey, clazz);
        String key = RedisConstants.createCacheKey(clazz, id);
        // 如果Redis中查询到数据 直接返回 否则执行查询数据库操作
        if (value != null) {
            log.debug("缓存已命中，从Redis取值返回，跳过访问数据库环节，其中Key:{} Value:{}", key, value);
            return value;
        } else {
            T t = action.apply(id);
            if (Objects.nonNull(t) && ms <= 0) {
                RedisUtils.save(cacheKey, t, 0, TimeUnit.SECONDS);
            } else if (Objects.nonNull(t)) {
                RedisUtils.save(cacheKey, t, ms, TimeUnit.MILLISECONDS);
            }
            log.debug("缓存未命中，已向Redis添加缓存，其中Key:{} Value:{}", key, t);
            return t;
        }
    }


    /**
     * 获取当前DO实体类主键值
     *
     * @param tableInfo 表信息实例
     * @param e         DO实体类
     * @param <E>       DO实体类类型
     * @param <R>       主键的类型
     * @return 主键值
     */
    @SuppressWarnings("unchecked")
    public static <E, R> R pkVal(TableInfo tableInfo, E e) {
        String keyProperty = tableInfo.getKeyProperty();
        return (R) tableInfo.getPropertyValue(e, keyProperty);
    }

    /**
     * 获取当前DO实体类主键值
     *
     * @param entity 实体类实例
     * @param clazz  实体类Class对象
     * @param <T>    实体类类型
     * @return 主键值
     */
    public static <T, S extends Serializable> S pkVal(T entity, Class<T> clazz) {
        TableInfo tableInfo = TableInfoHelper.getTableInfo(clazz);
        return pkVal(tableInfo, entity);
    }

    /**
     * 找出差异值 类型不确定 具体实现还是挺复杂的
     */
    private static <T> List<? extends Serializable> getRemainIds(Collection<? extends Serializable> ids, List<T> ts, TableInfo tableInfo) {
        Serializable id = ColUtils.toObj(ids);
        if (id instanceof String) {
            List<String> tsIds = EntityUtils.toList(ts, e -> pkVal(tableInfo, e));
            List<String> remainIds = EntityUtils.toList(ids, e -> (String) e);
            remainIds.removeAll(tsIds);
            return remainIds;
        } else if (id instanceof Number) {
            List<String> tsIds = EntityUtils.toList(ts, e -> String.format("%s", (Serializable) pkVal(tableInfo, e)));
            List<String> remainIds = EntityUtils.toList(ids, String::valueOf);
            remainIds.removeAll(tsIds);
            return remainIds;
        }

        return Collections.emptyList();
    }


    /**
     * 找出主键ID在集合a中却不在b中的元素
     *
     * @return
     */
    public static <T extends Serializable> List<String> subtract(Collection<T> a, Collection<T> b) {
        Serializable id = ColUtils.toObj(a);
        if (id instanceof String || id instanceof Number) {
            List<String> aa = EntityUtils.toList(a, e -> (String) e);
            List<String> bb = EntityUtils.toList(b, e -> (String) e);
            bb.removeAll(aa);
            return bb;
        }
        return Collections.emptyList();
    }
}
