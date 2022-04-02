package com.github.gagechan.server.config;

import static com.github.gagechan.server.misc.Const.APPLICATION_PROPERTIES;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.github.gagechan.server.ioc.BeanContainer;

import cn.hutool.core.io.resource.ResourceUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * The type App config.
 *
 * @author GageChan
 * @version : AppConfig.java, v 0.1 2022年04月01 20:39 GageChan
 */
@Slf4j
public class AppConfig {

    /**
     * Load from class path.
     */
    public static void loadFromClassPath() {
        AppConfigProperties config = BeanContainer.getBean(AppConfigProperties.class);
        Properties properties = new Properties();
        try (InputStream stream = ResourceUtil.getStream(APPLICATION_PROPERTIES)){
            properties.load(stream);
        } catch (Throwable e) {
            // ignore
            log.info("not found 'application.properties' in classpath");
        }
        initProperties(properties, config);
    }

    private static void initProperties(Properties properties, AppConfigProperties config) {
        config.setPort(Integer.valueOf(properties.getProperty("web.http.port", "8000")));
        config.setApplicationName(properties.getProperty("application.name", "FastServer"));
        config.setApplicationVersion(properties.getProperty("application.version", "V0.1"));
        config.setStaticPrefix(properties.getProperty("web.http.static.prefix", "static"));
        config.setTemplatePrefix(properties.getProperty("web.http.template.prefix", "template"));
        config.setTemplateSuffix(properties.getProperty("web.http.template.suffix", "html"));
    }

}
