package dk.aau.sw402F15.Exception;

/**
 * Created by sahb on 08/05/15.
 */
public abstract class RuntimeCompilerException extends RuntimeException {
    public RuntimeCompilerException() {
    }

    public RuntimeCompilerException(String message) {
        super(message);
    }

    public abstract void printError(String code);
}
