package dk.aau.sw402F15.Exception.SymbolTable;

import dk.aau.sw402F15.Exception.CompilerNodeException;
import dk.aau.sw402F15.parser.node.Node;

/**
 * Created by sahb on 07/05/15.
 */
public class SymbolAlreadyExistsException extends CompilerNodeException {
    public SymbolAlreadyExistsException(Node node) {
        this("Symbol already excists", node);
    }

    public SymbolAlreadyExistsException(String message, Node node) {
        super(message, node);
    }
}
