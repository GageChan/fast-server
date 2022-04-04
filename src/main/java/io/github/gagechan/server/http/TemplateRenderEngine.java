package io.github.gagechan.server.http;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import cn.hutool.core.util.StrUtil;
import io.github.gagechan.server.annotation.Bean;
import io.github.gagechan.server.config.AppConfigProperties;
import io.github.gagechan.server.ioc.InitializedBean;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : GageChan
 * @version : TemplateRenderEngine.java, v 0.1 2022年04月04 17:07 GageChan
 */
@Bean
@Slf4j
public class TemplateRenderEngine implements InitializedBean {

    public String render(Map<String, Object> attr, String htmlName) {
        if (attr == null) {
            throw new RuntimeException("the attr is null when render html.");
        }
        VelocityContext context = new VelocityContext();
        String ret;
        StringWriter sw = new StringWriter();
        try {
            AppConfigProperties config = AppConfigProperties.getInstance();
            Template tp = Velocity.getTemplate(config.getTemplatePrefix() + File.separator + htmlName
                                               + StrUtil.C_DOT + config.getTemplateSuffix());
            attr.forEach(context::put);
            tp.merge(context, sw);
        } catch (Throwable throwable) {
            log.error("render html error.", throwable);
            throw new RuntimeException(throwable);
        } finally {
            ret = sw.toString();
            try {
                sw.close();
            } catch (IOException e) {
                // ignore
            }
        }
        return ret;
    }

    @Override
    public void afterBeanInitialized() {
        Properties prop = new Properties();
        prop.put("file.resource.loader.class",
            "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.init(prop);
    }
}
