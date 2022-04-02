package com.github.gagechan.server.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The interface Bean.
 *
 * @author GageChan
 * @version  : Bean.java, v 0.1 2022年04月01 19:41 GageChan
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Bean {

}
