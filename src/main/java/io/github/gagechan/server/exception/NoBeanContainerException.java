package io.github.gagechan.server.exception;

/**
 * The type No bean container exception.
 * @author  : GageChan
 * @version  : NoBeanContainerException.java, v 0.1 2022年04月01 19:49 GageChan
 */
public class NoBeanContainerException extends RuntimeException {

    /**
    * Instantiates a new No bean container exception.
    *
    * @param message the message
    */
    public NoBeanContainerException(String message) {
        super(message);
    }
}
