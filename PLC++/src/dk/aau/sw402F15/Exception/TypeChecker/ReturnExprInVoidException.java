package dk.aau.sw402F15.Exception.TypeChecker;

import dk.aau.sw402F15.parser.node.Node;

/**
 * Created by sahb on 07/05/15.
 */
public class ReturnExprInVoidException extends TypeCheckerException {
    public ReturnExprInVoidException(Node node) {
        this("Cannot return value in void method", node);
    }

    public ReturnExprInVoidException(String message, Node node) {
        super(message, node);
    }
}