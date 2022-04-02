package com.github.gagechan.server;

import com.github.gagechan.server.ioc.BeanContainer;
import com.github.gagechan.server.ioc.UrlContainer;

import lombok.extern.slf4j.Slf4j;

/**
 * The type Main.
 *
 * @author GageChan
 * @version : Main.java, v 0.1 2022年04月01 19:40 GageChan
 */
@Slf4j
public class Main {
    /**
     * The entry point of application.
     *
     * @param clazz the clazz
     */
    public static void run(Class<?> clazz) {
        BeanContainer container = new UrlContainer();
        container.load(clazz);
    }
}
