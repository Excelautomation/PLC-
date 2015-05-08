package dk.aau.sw402F15.Exception;

/**
 * Created by sahb on 08/05/15.
 */
public class CompilerException extends Exception {
    public CompilerException(RuntimeCompilerException cause) {
        super(cause);
    }

    public void printError(String code) {
        ((RuntimeCompilerException)getCause()).printError(code);
    }

    @Override
    public StackTraceElement[] getStackTrace() {
        return getCause().getStackTrace();
    }
}
