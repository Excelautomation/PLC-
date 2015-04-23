package dk.aau.sw402F15.Exception;

/**
 * Created by sahb on 22/04/15.
 */
public abstract class CompilerException extends RuntimeException {
    protected CompilerException() {
    }

    protected CompilerException(String message) {
        super(message);
    }

    protected CompilerException(String message, Throwable cause) {
        super(message, cause);
    }

    protected CompilerException(Throwable cause) {
        super(cause);
    }

    protected CompilerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}


