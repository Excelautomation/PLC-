package dk.aau.sw402F15.Exception;

import dk.aau.sw402F15.parser.node.Node;

/**
 * Created by sahb on 22/04/15.
 */
public abstract class CompilerException extends RuntimeException {
    private Node node;

    public void printError() {

    }

    public CompilerException(Node node) {
        this.node = node;
    }

    public CompilerException(String message, Node node) {
        super(message);
        this.node = node;
    }
}


