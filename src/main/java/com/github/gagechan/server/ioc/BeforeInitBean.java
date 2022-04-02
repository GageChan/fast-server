package com.github.gagechan.server.ioc;

import java.util.Map;

/**
 * The interface Before init bean.
 * @author  : GageChan
 * @version : BeforeInitBean.java, v 0.1 2022年04月01 21:10 GageChan
 */
public interface BeforeInitBean {

    /**
    * Before init bean.
    *
    * @param beanMap the bean map
    */
    void beforeInitBean(Map<String, Object> beanMap);

}
