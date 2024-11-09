package com.psm.infrastructure.DB.cacheEnhance;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.github.yulichang.extension.mapping.base.MPJDeepService;
import com.psm.infrastructure.DB.support.CustomUpdateWrapper;
import com.psm.infrastructure.DB.utils.PlusUtils;
import com.psm.utils.Entity.EntityUtils;
import com.psm.utils.Guava.GuavaUtils;
import com.psm.utils.Lock.LockMap;
import com.psm.utils.Ref.RefUtils;
import com.psm.utils.spring.SpringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.function.Function;

public interface BaseDBRepository<T> extends MPJDeepService<T> {
    /**
     * <p>保存数据</p>
     *
     * @param entity       DO实体类实例
     * @param fkColumn     DO实体类关联外键列（方法引用表示）
     * @param serviceClazz 外键表对应的服务类的Class对象 需要实现{@link BaseDBRepository}接口
     * @param statColumn   统计更新字段（方法引用表示）
     * @param <D>          外键对应实体类泛型
     * @return 保存成功 返回true
     * @since 1.6.4
     */
    default <D> boolean save(T entity, SFunction<T, ? extends Serializable> fkColumn, Class<? extends BaseDBRepository<D>> serviceClazz, SFunction<D, ?> statColumn) {
        BaseDBRepository<D> service = SpringUtils.getBean(serviceClazz);
        return save(entity, fkColumn, service, statColumn);
    }

    /**
     * <p>保存数据</p>
     *
     * @param entity     DO实体类实例
     * @param fkColumn   DO实体类关联外键列（方法引用表示）
     * @param service    外键表对应的服务类的实例
     * @param statColumn 统计更新字段（方法引用表示）
     * @param <D>        外键对应实体类泛型
     * @return 保存成功 返回true
     * @since 1.6.4
     */
    @Transactional(rollbackFor = Exception.class)
    default <D> boolean save(T entity, SFunction<T, ? extends Serializable> fkColumn, BaseDBRepository<D> service, SFunction<D, ?> statColumn) {
        Serializable fkValue = EntityUtils.toObj(entity, fkColumn);

        synchronized (getEntityClass()) {
            boolean rSave = save(entity);
            Long count = count(new LambdaUpdateWrapper<T>().eq(fkColumn, fkValue));
            boolean rUpdate = service.updateById(fkValue, statColumn, count);
            return rSave && rUpdate;
        }
    }

    /**
     * <p>删除数据</p>
     *
     * @param entity       DO实体类实例
     * @param fkColumn     DO实体类关联外键列（方法引用表示）
     * @param serviceClazz 外键表对应的服务类的Class对象 需要实现{@link BaseDBRepository}接口
     * @param statColumn   统计更新字段（方法引用表示）
     * @param <D>          外键对应实体类泛型
     * @return 删除成功 返回true
     * @since 1.6.4
     */
    default <D> boolean removeById(T entity, SFunction<T, ? extends Serializable> fkColumn, Class<? extends BaseDBRepository<D>> serviceClazz, SFunction<D, ?> statColumn) {
        BaseDBRepository<D> service = SpringUtils.getBean(serviceClazz);
        return removeById(entity, fkColumn, service, statColumn);
    }

    /**
     * <p>删除数据</p>
     *
     * @param entity     DO实体类实例
     * @param fkColumn   DO实体类关联外键列（方法引用表示）
     * @param service    外键表对应的服务类的实例
     * @param statColumn 统计更新字段（方法引用表示）
     * @param <D>        外键对应实体类泛型
     * @return 删除成功 返回true
     * @since 1.6.4
     */
    @Transactional(rollbackFor = Exception.class)
    default <D> boolean removeById(T entity, SFunction<T, ? extends Serializable> fkColumn, BaseDBRepository<D> service, SFunction<D, ?> statColumn) {
        Serializable fkValue;
        Serializable fkValueTmp = EntityUtils.toObj(entity, fkColumn);
        // 如果关联外键为null 则从数据库中重新查出来 保证能够正确更新统计字段
        if (Objects.isNull(fkValueTmp)) {
            Serializable pkVal = PlusUtils.pkVal(entity, getEntityClass());
            fkValue = EntityUtils.toObj(getById(pkVal), fkColumn);
        } else {
            fkValue = fkValueTmp;
        }

        synchronized (getEntityClass()) {
            boolean result = removeById(entity);
            Long count = count(new LambdaUpdateWrapper<T>().eq(fkColumn, fkValue));
            return result && service.updateById(fkValue, statColumn, count);
        }
    }

    /**
     * <p>删除数据</p>
     *
     * @param id           主键ID
     * @param fkColumn     DO实体类关联外键列（方法引用表示）
     * @param fkValue      DO实体类关联外键值
     * @param serviceClazz 外键表对应的服务类的Class对象 需要实现{@link BaseDBRepository}接口
     * @param statColumn   统计更新字段（方法引用表示）
     * @param <D>          外键对应实体类泛型
     * @return 删除成功 返回true
     * @since 1.6.4
     */
    default <D> boolean removeById(Serializable id, SFunction<T, ?> fkColumn, Serializable fkValue, Class<? extends BaseDBRepository<D>> serviceClazz, SFunction<D, ?> statColumn) {
        BaseDBRepository<D> bean = SpringUtils.getBean(serviceClazz);
        return removeById(id, fkColumn, fkValue, bean, statColumn);
    }

    /**
     * <p>删除数据</p>
     *
     * @param id         主键ID
     * @param fkColumn   DO实体类关联外键列（方法引用表示）
     * @param fkValue    DO实体类关联外键值
     * @param service    外键表对应的服务类的实例
     * @param statColumn 统计更新字段（方法引用表示）
     * @param <D>        外键对应实体类泛型
     * @return 删除成功 返回true
     * @since 1.6.4
     */
    @Transactional(rollbackFor = Exception.class)
    default <D> boolean removeById(Serializable id, SFunction<T, ?> fkColumn, Serializable fkValue, BaseDBRepository<D> service, SFunction<D, ?> statColumn) {
        synchronized (getEntityClass()) {
            boolean result = removeById(id);
            Long count = count(new LambdaUpdateWrapper<T>().eq(fkColumn, fkValue));
            return result && service.updateById(fkValue, statColumn, count);
        }
    }

    /**
     * <p>通过主键ID来更新</p>
     * <p>待更新的值通过计算产生</p>
     *
     * @param id     主键ID
     * @param field  DO实体类属性字段
     * @param action 回调方法 获取即将更新的Value值
     * @return 如果更新成功 返回true
     * @since 1.6.4
     */
    default boolean updateById(Serializable id, SFunction<T, ?> field, Function<Serializable, ? extends Number> action) {
        String filedName = RefUtils.getFiledName(field);
        String columnName = GuavaUtils.toUnderScoreCase(filedName);
        return updateById(id, columnName, action);
    }

    /**
     * <p>通过主键ID来更新</p>
     * <p>待更新的值通过计算产生</p>
     *
     * @param id    主键ID
     * @param field DO实体类属性字段
     * @param value 即将更新的Value值
     * @return 如果更新成功 返回true
     * @since 1.6.4
     */
    default boolean updateById(Serializable id, SFunction<T, ?> field, Serializable value) {
        String filedName = RefUtils.getFiledName(field);
        String columnName = GuavaUtils.toUnderScoreCase(filedName);
        return updateById(id, columnName, value);
    }

    /**
     * <p>通过主键ID来更新</p>
     * <p>待更新的值通过计算产生</p>
     *
     * @param id     主键ID
     * @param column 数据库列字段
     * @param action 回调方法 获取即将更新的Value值
     * @return 如果更新成功 返回true
     * @since 1.6.4
     */
    default boolean updateById(Serializable id, String column, Function<Serializable, ? extends Number> action) {
        Number value = action.apply(id);
        return updateById(id, column, value);
    }

    /**
     * <p>通过主键ID来更新</p>
     * <p>待更新的值通过计算产生</p>
     *
     * @param id     主键ID
     * @param column 数据库列字段
     * @param value  即将更新的Value值
     * @return 如果更新成功 返回true
     * @since 1.6.4
     */
    default boolean updateById(Serializable id, String column, Serializable value) {
        TableInfo tableInfo = TableInfoHelper.getTableInfo(getEntityClass());

        UpdateWrapper<T> wrapper = new UpdateWrapper<T>()
                .set(column, value).eq(tableInfo.getKeyColumn(), id);
        return update(wrapper);
    }


    /**
     * <p>通过主键ID查询</p>
     * <p>冗余查询条件</p>
     *
     * @param id    主键ID
     * @param field DO实体类属性字段 通常是逻辑外键列
     * @param value field列对应的查询值
     * @return DO实例
     * @since 1.6.4
     */
    default T getById(Serializable id, SFunction<T, ?> field, Object value) {
        String filedName = RefUtils.getFiledName(field);
        String columnName = GuavaUtils.toUnderScoreCase(filedName);
        TableInfo tableInfo = TableInfoHelper.getTableInfo(getEntityClass());
        QueryWrapper<T> wrapper = new QueryWrapper<T>().eq(columnName, value).eq(tableInfo.getKeyColumn(), id);
        return getOne(wrapper);
    }

    /**
     * <p>通过主键ID自增指定列</p>
     * <p>默认步长为1</p>
     *
     * @param id    主键ID
     * @param field DO实体类属性字段
     * @return 更新成功 返回true
     * @since 1.6.4
     */
    default boolean increaseById(Serializable id, SFunction<T, ? extends Number> field) {
        return increaseById(id, field, 1);
    }

    /**
     * <p>通过主键ID自增指定列</p>
     * <p>自定义步长</p>
     *
     * @param id    主键ID
     * @param field DO实体类属性字段
     * @param step  步长
     * @return 更新成功 返回true
     * @since 1.6.4
     */
    default boolean increaseById(Serializable id, SFunction<T, ? extends Number> field, int step) {
        CustomUpdateWrapper<T> wrapper = new CustomUpdateWrapper<T>();
        TableInfo tableInfo = TableInfoHelper.getTableInfo(getEntityClass());
        String filedName = RefUtils.getFiledName(field);
        String columnName = GuavaUtils.toUnderScoreCase(filedName);
        wrapper.incr(columnName, step).eq(tableInfo.getKeyColumn(), id);
        return this.update(wrapper);
    }

    /**
     * <p>通过主键ID自减指定列</p>
     * <p>默认步长为1</p>
     *
     * @param id    主键ID
     * @param field DO实体类属性字段
     * @return 更新成功 返回true
     * @since 1.6.4
     */
    default boolean decreaseById(Serializable id, SFunction<T, ? extends Number> field) {
        return decreaseById(id, field, 1);
    }

    /**
     * <p>通过主键ID自减指定列</p>
     * <p>自定义步长</p>
     *
     * @param id    主键ID
     * @param field DO实体类属性字段
     * @param step  步长
     * @return 更新成功 返回true
     * @since 1.6.4
     */
    default boolean decreaseById(Serializable id, SFunction<T, ? extends Number> field, int step) {
        CustomUpdateWrapper<T> wrapper = new CustomUpdateWrapper<T>();
        TableInfo tableInfo = TableInfoHelper.getTableInfo(getEntityClass());
        String filedName = RefUtils.getFiledName(field);
        String columnName = GuavaUtils.toUnderScoreCase(filedName);
        wrapper.decr(columnName, step).eq(tableInfo.getKeyColumn(), id);
        return this.update(wrapper);
    }

    /**
     * 重载{@link BaseDBRepository#removeById(Serializable)}方法 增加Redis分布式缓存功能
     * 使用事务是为了保证数据库与Redis双写数据的一致性
     *
     * @param id    主键ID
     * @param clazz 实体类Class对象（参数为了方法的重载 不参与实际业务逻辑）
     * @return true表示删除成功
     */
    @Transactional(rollbackFor = Exception.class)
    default boolean removeById(Serializable id, Class<?> clazz) {
        synchronized (this) {
            return PlusUtils.removeById(id, getEntityClass(), this::removeById);
        }
    }

    /**
     * 重载{@link BaseDBRepository#removeById(Object)}方法 增加Redis分布式缓存功能
     * 使用事务是为了保证数据库与Redis双写数据的一致性
     *
     * @param entity {@code T}实体类对象
     * @param clazz  实体类Class对象（参数为了方法的重载 不参与实际业务逻辑）
     * @return true表示删除成功
     */
    @Transactional(rollbackFor = Exception.class)
    default boolean removeById(T entity, Class<?> clazz) {
        synchronized (this) {
            return PlusUtils.removeById(entity, getEntityClass(), this::removeById);
        }
    }

    /**
     * 重载{@link BaseDBRepository#removeByIds(Collection)}方法 增加Redis分布式缓存功能
     * 使用事务是为了保证数据库与Redis双写数据的一致性
     *
     * @param idList 批主键ID
     * @param clazz  实体类Class对象（参数为了方法的重载 不参与实际业务逻辑）
     * @return true表示删除成功
     */
    @Transactional(rollbackFor = Exception.class)
    default boolean removeByIds(Collection<? extends Serializable> idList, Class<?> clazz) {
        synchronized (this) {
            return PlusUtils.removeByIds(idList, getEntityClass(), this::removeByIds);
        }
    }

    /**
     * <p>重载{@link BaseDBRepository#updateById(Object)}方法 增加Redis分布式缓存功能</p>
     * <p>使用事务是为了保证数据库与Redis缓存双写数据的一致性</p>
     * <p>使用JVM锁默认并发不高，否则请使用分布式锁</p>
     * <p>此处使用表锁 即为每一个服务类分配一把可重入锁 采用懒汉单例模式</p>
     *
     * @param entity DO实体类对象
     * @param clazz  实体类Class对象（参数为了方法的重载 不参与实际业务逻辑）
     * @return true表示更新成功
     */
    @Transactional(rollbackFor = Exception.class)
    default boolean updateById(T entity, Class<?> clazz) {
        // 此处使用表锁 并发更新时 写与读均阻塞
        Class<T> entityClass = getEntityClass();
        synchronized (this) {
            return PlusUtils.updateById(entity, entityClass, this::updateById, this::getById);
        }
    }

    /**
     * <p>重载{@link BaseDBRepository#updateById(Object)}方法 增加Redis分布式缓存功能</p>
     * <p>使用事务是为了保证数据库与Redis缓存双写数据的一致性</p>
     * <p>使用JVM锁默认并发不高，否则请使用分布式锁</p>
     * <p>此处使用表锁 即为每一个服务类分配一把可重入锁 采用懒汉单例模式</p>
     *
     * @param entity DO实体类对象
     * @param clazz  实体类Class对象（参数为了方法的重载 不参与实际业务逻辑）
     * @return true表示更新成功
     */
    @Transactional(rollbackFor = Exception.class)
    default boolean saveOrUpdate(T entity, Class<?> clazz) {
        // 此处使用表锁 并发更新时 写与读均阻塞
        Class<T> entityClass = getEntityClass();
        synchronized (this) {
            return PlusUtils.saveOrUpdate(entity, entityClass, this::saveOrUpdate);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    default boolean saveOrUpdateBatch(Collection<T> entityList, Class<?> clazz) {
        // 此处使用表锁 并发更新时 写与读均阻塞
        Class<T> entityClass = getEntityClass();
        synchronized (this) {
            return PlusUtils.saveOrUpdateBatch(entityList, entityClass, this::saveOrUpdateBatch);
        }
    }

    /**
     * <p>重载{@link BaseDBRepository#updateById(Object)}方法 增加Redis分布式缓存功能</p>
     * <p>使用事务是为了保证数据库与Redis缓存双写数据的一致性</p>
     * <p>使用JVM锁默认并发不高，否则请使用分布式锁</p>
     * <p>此处使用表锁 即为每一个服务类分配一把可重入锁 采用懒汉单例模式</p>
     *
     * @param entity DO实体类对象
     * @param clazz  实体类Class对象（参数为了方法的重载 不参与实际业务逻辑）
     * @param lock   如果置空则使用默认可重入锁，否则使用参数给定的锁
     * @return true表示更新成功
     */
    @Transactional(rollbackFor = Exception.class)
    default boolean updateById(T entity, Class<?> clazz, Lock lock) {
        Lock optLock = lock != null ? lock : getJvmLock();
        // 此处使用表锁 并发更新时 写与读均阻塞
        Class<T> entityClass = getEntityClass();
        return PlusUtils.updateById(entity, entityClass, this::updateById, this::getById, optLock);
    }

    /**
     * <p>重载{@link BaseDBRepository#getById(Serializable)}方法 增加Redis分布式缓存功能</p>
     * <p>有缓存；无过期时间；适合并发较低的场景</p>
     *
     * @param id    主键ID
     * @param clazz 实体类Class对象（参数为了方法的重载 不参与实际业务逻辑）
     * @return {@code T}实体类对象实例
     */
    default T getById(Serializable id, Class<?> clazz) {
        return PlusUtils.ifNotCache(id, getEntityClass(), this::getById, getJvmLock());
    }

    /**
     * 重载{@link BaseDBRepository#getById(Serializable)}方法 增加Redis分布式缓存功能
     * <p>有缓存；有过期时间；适合并发较低的场景</p>
     *
     * @param id 主键ID
     * @param ms 过期时间 毫秒
     * @return {@code T}实体类对象实例
     */
    default T getById(Serializable id, long ms) {
        return PlusUtils.ifNotCache(id, getEntityClass(), ms, this::getById, getJvmLock());
    }

    /**
     * 重载{@link BaseDBRepository#getById(Serializable)}方法 增加Redis分布式缓存功能
     *
     * @param id    主键ID
     * @param ms    过期时间 毫秒
     * @param clazz 实体类Class对象（参数为了方法的重载 不参与实际业务逻辑）
     * @return {@code T}实体类对象实例
     */
    default T getById(Serializable id, long ms, Class<?> clazz) {
        return PlusUtils.ifNotCache(id, getEntityClass(), ms, this::getById, getJvmLock());
    }


    /**
     * 重载{@link BaseDBRepository#listByIds(Collection)}方法 增加Redis分布式缓存功能
     *
     * @param ids   批主键ID
     * @param clazz 实体类Class对象（参数为了方法的重载 不参与实际业务逻辑）
     * @return 以{@code T}实体类为元素的集合实例
     */
    default List<T> listByIds(Collection<? extends Serializable> ids, Class<?> clazz) {
        return PlusUtils.ifNotCache(ids, getEntityClass(), -1, this::listByIds);
    }

    /**
     * 重载{@link BaseDBRepository#listByIds(Collection)}方法 增加Redis分布式缓存功能
     *
     * @param ids 批主键ID
     * @param ms  过期时间 毫秒
     * @return 以{@code T}实体类为元素的集合实例
     */
    default List<T> listByIds(Collection<? extends Serializable> ids, long ms) {
        return PlusUtils.ifNotCache(ids, getEntityClass(), ms, this::listByIds);
    }

    /**
     * 重载{@link BaseDBRepository#listByIds(Collection)}方法 增加Redis分布式缓存功能
     *
     * @param ids   批主键ID
     * @param clazz 实体类Class对象（参数为了方法的重载 不参与实际业务逻辑）
     * @param ms    过期时间 毫秒
     * @return 以{@code T}实体类为元素的集合实例
     */
    default List<T> listByIds(Collection<? extends Serializable> ids, long ms, Class<?> clazz) {
        return PlusUtils.ifNotCache(ids, getEntityClass(), ms, this::listByIds);
    }


    /**
     * 获取实体类表锁
     *
     * @return 重入锁实例
     */
    default Lock getJvmLock() {
        return LockMap.getLocalLock(getEntityClass());
    }

    /**
     * <p>重载{@link BaseDBRepository#getById(Serializable)}方法 增加Redis分布式缓存功能</p>
     * <p>增加分布式锁的支持 防止高并发场景下<i>缓存穿透</i></p>
     *
     * @param id       主键ID
     * @param ms       过期时间 毫秒
     * @param redisson {@link Redisson}实例
     * @return {@code T}实体类对象实例
     */
    default T getById(Serializable id, long ms, RedissonClient redisson) {
        return PlusUtils.ifNotCache(id, getEntityClass(), ms, this::getById, redisson);
    }
}
