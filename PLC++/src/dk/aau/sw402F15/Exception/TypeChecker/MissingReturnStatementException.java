package dk.aau.sw402F15.Exception.TypeChecker;

import dk.aau.sw402F15.parser.node.Node;

/**
 * Created by sahb on 07/05/15.
 */
public class MissingReturnStatementException extends TypeCheckerException {
    public MissingReturnStatementException(Node node) {
        super(node);
    }

    public MissingReturnStatementException(String message, Node node) {
        super(message, node);
    }
}
