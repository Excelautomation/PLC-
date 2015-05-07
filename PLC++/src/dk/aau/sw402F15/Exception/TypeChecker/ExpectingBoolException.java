package dk.aau.sw402F15.Exception.TypeChecker;

import dk.aau.sw402F15.Symboltable.Type.SymbolType;
import dk.aau.sw402F15.parser.node.Node;

/**
 * Created by sahb on 07/05/15.
 */
public class ExpectingBoolException extends TypeCheckerException {
    private final SymbolType type;

    public ExpectingBoolException(Node node, SymbolType type) {
        super("Expecting bool", node);
        this.type = type;
    }

    public ExpectingBoolException(String message, Node node, SymbolType type) {
        super(message, node);
        this.type = type;
    }
}

