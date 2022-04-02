package com.github.gagechan.server.ioc;

/**
 * The interface Bean post process.
 * @author  : GageChan
 * @version : BeanPostProcess.java, v 0.1 2022年04月01 20:00 GageChan
 */
public interface BeanPostProcess {

    /**
    * Post process.
    */
    void postProcess();

    /**
    * Gets order.
    *
    * @return the order
    */
    default int getOrder() {
        return 0;
    }

}
