package dk.aau.sw402F15.Exception.ScopeChecker;

import dk.aau.sw402F15.Exception.CompilerException;
import dk.aau.sw402F15.parser.node.Node;

/**
 * Created by sahb on 07/05/15.
 */
public abstract class ScopeCheckerException extends CompilerException {
    public ScopeCheckerException(String message, Node node) {
        super(message, node);
    }
}
