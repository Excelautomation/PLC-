package dk.aau.sw402F15.Exception.TypeChecker;

import dk.aau.sw402F15.Exception.CompilerException;
import dk.aau.sw402F15.parser.node.Node;

/**
 * Created by sahb on 07/05/15.
 */
public abstract class TypeCheckerException extends CompilerException {
    public TypeCheckerException() {
    }

    public TypeCheckerException(String message) {
        super(message);
    }

    protected TypeCheckerException(Node node) {
        super(node);
    }

    protected TypeCheckerException(String message, Node node) {
        super(message, node);
    }
}

