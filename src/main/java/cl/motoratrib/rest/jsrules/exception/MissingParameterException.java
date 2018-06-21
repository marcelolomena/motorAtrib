package cl.motoratrib.rest.jsrules.exception;

import cl.motoratrib.rest.jsrules.exception.InvalidParameterException;

/**
 * Created by Marcelo Lomeña 4/30/2018
 */
public class MissingParameterException extends InvalidParameterException {
    public MissingParameterException() {
        super();
    }

    public MissingParameterException(String message) {
        super(message);
    }

    public MissingParameterException(String message, Throwable cause) {
        super(message, cause);
    }

    public MissingParameterException(Throwable cause) {
        super(cause);
    }

    public MissingParameterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
