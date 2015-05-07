package dk.aau.sw402F15.Exception.TypeChecker;

import dk.aau.sw402F15.parser.node.Node;

public class RedefinitionOfConstException extends TypeCheckerException {
    public RedefinitionOfConstException() {
    }

    public RedefinitionOfConstException(String message) {
        super(message);
    }

    public RedefinitionOfConstException(Node node) {
        super(node);
    }

    public RedefinitionOfConstException(String message, Node node) {
        super(message, node);
    }
}
