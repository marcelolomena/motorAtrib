package cl.motoratrib.rest.jsrules.exception;

import cl.motoratrib.rest.jsrules.exception.InvalidParameterException;

/**
 * Created by Marcelo Lomeña 4/30/2018
 */
public class InvalidParameterClassException extends InvalidParameterException {
    public InvalidParameterClassException() {
        super();
    }

    public InvalidParameterClassException(String message) {
        super(message);
    }

    public InvalidParameterClassException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidParameterClassException(Throwable cause) {
        super(cause);
    }

    public InvalidParameterClassException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
