package dk.aau.sw402F15.Exception.TypeChecker;

import dk.aau.sw402F15.parser.node.Node;

public class ReturnContainsNoExprException extends TypeCheckerException {
    public ReturnContainsNoExprException(Node node) {
        this("Missing return value", node);
    }

    public ReturnContainsNoExprException(String message, Node node) {
        super(message, node);
    }
}
