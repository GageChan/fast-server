package com.github.gagechan.server.exception;

/**
 * The type Init bean exception.
 * @author  : GageChan
 * @version  : InitBeanException.java, v 0.1 2022年04月01 19:57 GageChan
 */
public class InitBeanException extends RuntimeException {

    /**
    * Instantiates a new Init bean exception.
    *
    * @param message the message
    * @param cause the cause
    */
    public InitBeanException(String message, Throwable cause) {
        super(message, cause);
    }
}
