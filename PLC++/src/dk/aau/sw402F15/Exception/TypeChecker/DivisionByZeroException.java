package dk.aau.sw402F15.Exception.TypeChecker;

import dk.aau.sw402F15.parser.node.Node;

/**
 * Created by sahb on 07/05/15.
 */
public class DivisionByZeroException extends TypeCheckerException {
    public DivisionByZeroException(Node node) {
        super(node);
    }

    public DivisionByZeroException(String message, Node node) {
        super(message, node);
    }
}
