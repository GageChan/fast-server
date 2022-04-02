package com.github.gagechan.server.exception;

/**
 * The type No unique url exception.
 * @author  : GageChan
 * @version : NoUniqueUrlException.java, v 0.1 2022年04月01 20:32 GageChan
 */
public class NoUniqueUrlException extends RuntimeException{

    /**
    * Instantiates a new No unique url exception.
    *
    * @param message the message
    */
    public NoUniqueUrlException(String message) {
        super(message);
    }
}
