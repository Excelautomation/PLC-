package dk.aau.sw402F15.Exception;

import dk.aau.sw402F15.parser.node.Node;

public class CompilerInternalNodeException extends CompilerNodeException {

    public CompilerInternalNodeException(String message, Node node) {
        super(message, node);
    }
}
