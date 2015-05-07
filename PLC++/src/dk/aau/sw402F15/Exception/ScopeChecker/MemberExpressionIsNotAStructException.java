package dk.aau.sw402F15.Exception.ScopeChecker;

import dk.aau.sw402F15.parser.node.Node;

/**
 * Created by sahb on 07/05/15.
 */
public class MemberExpressionIsNotAStructException extends ScopeCheckerException {
    public MemberExpressionIsNotAStructException(Node node) {
        super("Not valid member expression", node);
    }

    public MemberExpressionIsNotAStructException(String message, Node node) {
        super(message, node);
    }
}
