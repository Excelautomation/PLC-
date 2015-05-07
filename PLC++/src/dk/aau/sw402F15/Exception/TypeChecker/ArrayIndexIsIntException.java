package dk.aau.sw402F15.Exception.TypeChecker;

import dk.aau.sw402F15.parser.node.Node;

/**
 * Created by sahb on 07/05/15.
 */
public class ArrayIndexIsIntException extends TypeCheckerException {
    public ArrayIndexIsIntException(Node node) {
        this("Array index needs to be a int", node);
    }

    public ArrayIndexIsIntException(String message, Node node) {
        super(message, node);
    }
}
