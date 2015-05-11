package dk.aau.sw402F15.Exception.TypeChecker;

import dk.aau.sw402F15.Exception.CompilerNodeException;
import dk.aau.sw402F15.parser.node.Node;

/**
 * Created by sahb on 11/05/15.
 */
public class InvalidPortException extends CompilerNodeException {
    public InvalidPortException(Node node) {
        super("Invalid port", node);
    }
}
