package com.psm.utils.spring;

import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
public class SpringUtils implements BeanFactoryPostProcessor, ApplicationContextAware {
    /**
     * Spring应用上下文环境
     */
    private static ConfigurableListableBeanFactory beanFactory;
    private static ApplicationContext applicationContext;

    private SpringUtils() {
    }

    /**
     * 获取对象
     *
     * @param name 实例名称
     * @return Object 一个以所给名字注册的bean的实例
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) throws BeansException {
        return (T) beanFactory.getBean(name);
    }


    /**
     * 刷新bean
     * <p>
     * 刷新bean适用于动态的修改bean内容
     * <p>
     * 用新bean替换原来的旧bean
     *
     * @param name bean名称
     * @param bean 新的bean实例
     */
    public static void refreshBean(String name, Object bean) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(bean);
        //获取上下文
        DefaultListableBeanFactory defaultListableBeanFactory =
                (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
        //销毁指定实例
        defaultListableBeanFactory.destroySingleton(name);
        //重新注册同名实例，这样在其他地方注入的实例还是同一个名称，但是实例内容已经重新加载
        defaultListableBeanFactory.registerSingleton(name, bean);
    }

    /**
     * 获取类型为requiredType的对象
     *
     * @param clazz 实例类型
     * @return bean的实例
     */
    public static <T> T getBean(Class<T> clazz) throws BeansException {
        T result = beanFactory.getBean(clazz);
        return result;
    }


    /**
     * 返回指定类型所有的子类
     *
     * @param clazz 接口或者抽象类对象
     * @param <T>   接口或者抽象类类型
     * @return 集合
     */
    public static <T> List<T> getBeans(Class<T> clazz) {
        List<T> list = new ArrayList<T>();
        Map<String, T> beansOfType = applicationContext.getBeansOfType(clazz);
        beansOfType.forEach((k, v) -> list.add(v));
        return list;
    }

    /**
     * 如果BeanFactory包含一个与所给名称匹配的bean定义，则返回true
     *
     * @param name 实例名称
     * @return boolean
     */
    public static boolean containsBean(String name) {
        return beanFactory.containsBean(name);
    }

    /**
     * 判断以给定名字注册的bean定义是一个singleton还是一个prototype。 如果与给定名字相应的bean定义没有被找到，将会抛出一个异常（NoSuchBeanDefinitionException）
     *
     * @param name 实例名称
     * @return boolean
     */
    public static boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
        return beanFactory.isSingleton(name);
    }

    /**
     * @param name 实例名称
     * @return Class 注册对象的类型
     */
    public static Class<?> getType(String name) throws NoSuchBeanDefinitionException {
        return beanFactory.getType(name);
    }

    /**
     * 如果给定的bean名字在bean定义中有别名，则返回这些别名
     *
     * @param name 实例名称
     * @return String[]
     */
    public static String[] getAliases(String name) throws NoSuchBeanDefinitionException {
        return beanFactory.getAliases(name);
    }

    /**
     * 获取aop代理对象
     *
     * @param invoker
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T getAopProxy(T invoker) {
        return (T) AopContext.currentProxy();
    }

    /**
     * 获取当前的环境配置，无配置返回null
     *
     * @return 当前的环境配置
     */
    public static String[] getActiveProfiles() {
        return applicationContext.getEnvironment().getActiveProfiles();
    }

    /**
     * 获取当前的环境配置，当有多个环境配置时，只获取第一个
     *
     * @return 当前的环境配置
     */
    public static String getActiveProfile() {
        final String[] activeProfiles = getActiveProfiles();
        return activeProfiles.length > 0 ? activeProfiles[0] : null;
    }

    /**
     * @param beanFactory ConfigurableListableBeanFactory
     * @throws BeansException BeansException
     */
    @Override
    public void postProcessBeanFactory(@NonNull ConfigurableListableBeanFactory beanFactory) throws BeansException {
        SpringUtils.beanFactory = beanFactory;
    }

    /**
     * @param applicationContext ApplicationContext
     * @throws BeansException BeansException
     */
    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        SpringUtils.applicationContext = applicationContext;
    }
}
