package com.github.gagechan.server.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cn.hutool.core.util.StrUtil;

/**
 * The interface Enable ioc.
 *
 * @author GageChan
 * @version : EnableIoc.java, v 0.1 2022年04月01 19:43 GageChan
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EnableIoc {

    /**
     * Scan base package string.
     *
     * @return the string
     */
    String scanBasePackage() default StrUtil.EMPTY;

    /**
     * Exclude classes class [ ].
     *
     * @return the class [ ]
     */
    Class<?>[] excludeClasses() default {};

}
