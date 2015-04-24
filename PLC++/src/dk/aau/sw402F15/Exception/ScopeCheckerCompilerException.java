package dk.aau.sw402F15.Exception;

public abstract class ScopeCheckerCompilerException extends CompilerException {
    protected ScopeCheckerCompilerException(String message, Throwable cause) {
        super(message, cause);
    }

    protected ScopeCheckerCompilerException(String message) {
        super(message);
    }
}
