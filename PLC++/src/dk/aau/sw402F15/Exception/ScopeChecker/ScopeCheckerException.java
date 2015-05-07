package dk.aau.sw402F15.Exception.ScopeChecker;

import dk.aau.sw402F15.Exception.CompilerException;
import dk.aau.sw402F15.parser.node.Node;

/**
 * Created by sahb on 07/05/15.
 */
public class ScopeCheckerException extends CompilerException {
    public ScopeCheckerException(String message) {
        super(message);
    }

    public ScopeCheckerException() {
    }

    public ScopeCheckerException(Node node) {
        super(node);
    }

    public ScopeCheckerException(String message, Node node) {
        super(message, node);
    }
}
