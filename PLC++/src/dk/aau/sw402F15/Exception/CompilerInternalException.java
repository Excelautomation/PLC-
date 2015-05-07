package dk.aau.sw402F15.Exception;

import dk.aau.sw402F15.parser.node.Node;

public class CompilerInternalException extends CompilerException {

    public CompilerInternalException(String message, Node node) {
        super(message, node);
    }
}
