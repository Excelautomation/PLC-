package dk.aau.sw402F15.Exception;

/**
 * Created by sahb on 23/04/15.
 */
public abstract class CompilerSymbolTableException extends CompilerException {
    protected CompilerSymbolTableException() {
    }

    protected CompilerSymbolTableException(String message) {
        super(message);
    }

    protected CompilerSymbolTableException(String message, Throwable cause) {
        super(message, cause);
    }

    protected CompilerSymbolTableException(Throwable cause) {
        super(cause);
    }

    protected CompilerSymbolTableException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
