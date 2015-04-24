package dk.aau.sw402F15.Exception;

/**
 * Created by sahb on 24/04/15.
 */
public class MemberExpressionNotValidScopeCheckerException extends ScopeCheckerCompilerException {
    public MemberExpressionNotValidScopeCheckerException(String message) {
        super(message);
    }

    protected MemberExpressionNotValidScopeCheckerException(String message, Throwable cause) {
        super(message, cause);
    }
}
