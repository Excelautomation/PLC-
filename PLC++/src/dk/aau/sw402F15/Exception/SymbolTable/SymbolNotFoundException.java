package dk.aau.sw402F15.Exception.SymbolTable;

import dk.aau.sw402F15.Exception.CompilerNodeException;
import dk.aau.sw402F15.parser.node.Node;

/**
 * Created by sahb on 07/05/15.
 */
public class SymbolNotFoundException extends CompilerNodeException {
    public SymbolNotFoundException(Node node) {
        super("Symbol was not found", node);
    }

    public SymbolNotFoundException(String message, Node node) {
        super(message, node);
    }
}
