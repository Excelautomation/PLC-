package dk.aau.sw402F15.Exception.TypeChecker;

import dk.aau.sw402F15.parser.node.Node;

/**
 * Created by sahb on 07/05/15.
 */
public class ArrayIndexOfNonArrayException extends TypeCheckerException {
    public ArrayIndexOfNonArrayException(Node node) {
        super(node);
    }

    public ArrayIndexOfNonArrayException(String message, Node node) {
        super(message, node);
    }
}
