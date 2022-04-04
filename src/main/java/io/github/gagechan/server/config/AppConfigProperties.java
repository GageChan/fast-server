package io.github.gagechan.server.config;

import io.github.gagechan.server.annotation.Bean;
import io.github.gagechan.server.ioc.BeanContainer;
import io.github.gagechan.server.ioc.InitializedBean;

import cn.hutool.json.JSONUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * The type App config properties.
 *
 * @author GageChan
 * @version  : AppConfigProperties.java, v 0.1 2022年04月01 20:39 GageChan
 */
@Data
@Bean
@Slf4j
public class AppConfigProperties implements InitializedBean {

    private Integer port;

    private String  applicationName;

    private String  applicationVersion;

    private String  staticPrefix;

    private String  templatePrefix;

    private String  templateSuffix;

    /**
     * Gets instance.
     *
     * @return the instance
    */
    public static AppConfigProperties getInstance() {
        return BeanContainer.getBean(AppConfigProperties.class);
    }

    @Override
    public void afterBeanInitialized() {
        AppConfig.loadFromClassPath();
        log.info("load config: ");
        AppConfigProperties config = getInstance();
        System.out.println(JSONUtil.toJsonPrettyStr(config));
    }

    public Map<String, Object> getConfigs() {
        return JSONUtil.parseObj(this).toBean(Map.class);
    }

}
