package dk.aau.sw402F15.Exception;

/**
 * Created by sahb on 24/04/15.
 */
public abstract class PreprocessorCompilerException extends CompilerException {
    protected PreprocessorCompilerException(String message) {
        super(message);
    }

    protected PreprocessorCompilerException(String message, Throwable cause) {
        super(message, cause);
    }
}

