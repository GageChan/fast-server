package io.github.gagechan.server.exception;

/**
 * The type No such bean exception.
 * @author  : GageChan
 * @version  : NoSuchBeanException.java, v 0.1 2022年04月01 20:45 GageChan
 */
public class NoSuchBeanException extends RuntimeException {

    /**
    * Instantiates a new No such bean exception.
    *
    * @param message the message
    */
    public NoSuchBeanException(String message) {
        super(message);
    }
}
