package dk.aau.sw402F15.Exception.TypeChecker;

import dk.aau.sw402F15.parser.node.Node;

public class RedefinitionOfConstException extends TypeCheckerException {
    public RedefinitionOfConstException(Node node) {
        this("Cannot assign a value to const variable", node);
    }

    public RedefinitionOfConstException(String message, Node node) {
        super(message, node);
    }
}
