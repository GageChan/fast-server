package com.github.gagechan.server.ioc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.github.gagechan.server.annotation.Bean;
import com.github.gagechan.server.annotation.EnableIoc;
import com.github.gagechan.server.annotation.Route;
import com.github.gagechan.server.exception.InitBeanException;
import com.github.gagechan.server.exception.NoBeanContainerException;
import com.github.gagechan.server.exception.NoSuchBeanException;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * The type Bean container.
 * @author  : GageChan
 * @version : BeanContainer.java, v 0.1 2022年04月01 19:45 GageChan
 */
@Slf4j
public abstract class BeanContainer {

    private static final String                  FAST_PACKAGE = "com.github.gagechan.server";

    /**
     * The Bean map.
     */
    private static final Map<String, Object>     beanMap      = new ConcurrentHashMap<>();

    /**
     * The constant urlMap.
     */
    protected static final Map<String, Class<?>> urlMap       = new ConcurrentHashMap<>();

    /**
     * Gets bean.
     *
     * @param <T>   the type parameter
    * @param clazz the clazz
    * @return the bean
    */
    public static <T> T getBean(Class<T> clazz) {
        if (!beanMap.containsKey(clazz.getName())) {
            throw new NoSuchBeanException("no bean " + clazz.getName() + " in ioc.");
        }
        return (T) beanMap.get(clazz.getName());
    }

    /**
     * Load.
     */
    public void load(Class<?> clazz) {
        log.info("starting load ioc...");
        Set<Class<?>> mainClasses = ClassUtil.scanPackageByAnnotation(clazz.getName(),
            EnableIoc.class);
        if (mainClasses == null || mainClasses.size() == 0) {
            throw new NoBeanContainerException("please use @EnableIoc to enable ioc!");
        }
        Class<?> mainClass = mainClasses.iterator().next();
        EnableIoc enableIoc = mainClass.getAnnotation(EnableIoc.class);
        String scanBasePackage = StrUtil.EMPTY;
        if (StrUtil.isEmpty(enableIoc.scanBasePackage())) {
            scanBasePackage = mainClass.getPackage().getName();
        }
        Class<?>[] excludeClasses = enableIoc.excludeClasses();
        try {
            initBean(scanBasePackage, excludeClasses);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new InitBeanException("init bean error.", e);
        }
    }

    private void initBean(String scanBasePackage,
                          Class<?>[] excludeClasses) throws InstantiationException,
                                                     IllegalAccessException {
        Set<Class<?>> otherBeanClasses = ClassUtil.scanPackage(scanBasePackage,
            clazz -> clazz.isAnnotationPresent(Bean.class)
                     || clazz.isAnnotationPresent(Route.class));
        Set<Class<?>> beanClasses = new HashSet<>(otherBeanClasses);
        // 加载自身的bean
        if (!StrUtil.equals(FAST_PACKAGE, scanBasePackage)) {
            Set<Class<?>> fastServerBeans = ClassUtil.scanPackage(FAST_PACKAGE,
                clazz -> clazz.isAnnotationPresent(Bean.class)
                         || clazz.isAnnotationPresent(Route.class));
            beanClasses.addAll(fastServerBeans);
        }

        Set<Class<?>> finalBeanClasses = beanClasses.stream()
            .filter(clazz -> !Arrays.asList(excludeClasses).contains(clazz))
            .filter(ClassUtil::isNormalClass).collect(Collectors.toSet());

        List<BeanPostProcess> beanPostProcessList = new ArrayList<>();
        for (Class<?> clazz : finalBeanClasses) {
            // before bean initialize
            if (Arrays.asList(clazz.getInterfaces()).contains(BeforeInitBean.class)) {
                ClassUtil.invoke(clazz.getName(), "beforeInitBean", true, beanMap);
            }
            String key = clazz.getName();
            Object obj = clazz.newInstance();
            beanMap.put(key, obj);
            // after bean initialized
            if (Arrays.asList(clazz.getInterfaces()).contains(InitializedBean.class)) {
                ClassUtil.invoke(clazz.getName(), "afterBeanInitialized", true);
            }
            if (Arrays.asList(clazz.getInterfaces()).contains(BeanPostProcess.class)) {
                beanPostProcessList.add((BeanPostProcess) obj);
            }
            loadRoute(clazz);
        }
        log.info("There are {} beans initialized.", beanMap.size());
        // bean post process
        beanPostProcessList.sort(Comparator.comparingInt(BeanPostProcess::getOrder).reversed());
        for (BeanPostProcess beanPostProcess : beanPostProcessList) {
            beanPostProcess.postProcess();
        }

    }

    /**
     * Load route *
     * @param clazz clazz
    */
    protected abstract void loadRoute(Class<?> clazz);

}
