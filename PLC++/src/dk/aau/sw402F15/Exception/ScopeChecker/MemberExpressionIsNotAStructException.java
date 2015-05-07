package dk.aau.sw402F15.Exception.ScopeChecker;

import dk.aau.sw402F15.parser.node.Node;

/**
 * Created by sahb on 07/05/15.
 */
public class MemberExpressionIsNotAStructException extends ScopeCheckerException {
    public MemberExpressionIsNotAStructException() {
    }

    public MemberExpressionIsNotAStructException(String message) {
        super(message);
    }

    public MemberExpressionIsNotAStructException(Node node) {
        super(node);
    }

    public MemberExpressionIsNotAStructException(String message, Node node) {
        super(message, node);
    }
}
