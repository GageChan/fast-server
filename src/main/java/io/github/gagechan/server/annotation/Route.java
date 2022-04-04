package io.github.gagechan.server.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cn.hutool.core.util.StrUtil;

/**
 * The interface Route.
 *
 * @author GageChan
 * @version  : Route.java, v 0.1 2022年04月01 19:44 GageChan
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Route {

    /**
     * Path string.
     *
     * @return the string
    */
    String path() default StrUtil.EMPTY;

}
