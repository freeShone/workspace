package com.base.rabbit.api.exception;

/**
 * $MessageException
 * @author shaohua
 */
public class MessageException extends Exception {

    private static final long serialVersionUID = 2051249984085733850L;

    public MessageException() {
        super();
    }

    public MessageException(String message) {
        super(message);
    }

    public MessageException(String message, Throwable cause) {
        super(message, cause);
    }

    public MessageException(Throwable cause) {
        super(cause);
    }
}
