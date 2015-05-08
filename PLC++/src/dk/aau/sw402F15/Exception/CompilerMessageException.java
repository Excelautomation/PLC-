package dk.aau.sw402F15.Exception;

/**
 * Created by sahb on 08/05/15.
 */
public abstract class CompilerMessageException extends RuntimeCompilerException {
    protected CompilerMessageException(String message) {
        super(message);
    }

    @Override
    public void printError(String code) {
        System.err.println(getMessage());
    }
}
