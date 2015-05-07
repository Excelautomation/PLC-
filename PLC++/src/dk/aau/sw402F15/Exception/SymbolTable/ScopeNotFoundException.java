package dk.aau.sw402F15.Exception.SymbolTable;

import dk.aau.sw402F15.Exception.CompilerException;
import dk.aau.sw402F15.parser.node.Node;

/**
 * Created by sahb on 07/05/15.
 */
public class ScopeNotFoundException extends CompilerException {
    public ScopeNotFoundException(Node node) {
        super(node);
    }

    public ScopeNotFoundException(String message, Node node) {
        super(message, node);
    }
}
