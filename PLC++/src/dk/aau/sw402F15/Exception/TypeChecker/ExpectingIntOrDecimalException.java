package dk.aau.sw402F15.Exception.TypeChecker;

import dk.aau.sw402F15.Symboltable.Type.SymbolType;
import dk.aau.sw402F15.parser.node.Node;

public class ExpectingIntOrDecimalException extends TypeCheckerException {
    private final SymbolType type;

    public ExpectingIntOrDecimalException(Node node, SymbolType type) {
        this("Expecting integer", node, type);
    }

    public ExpectingIntOrDecimalException(String message, Node node, SymbolType type) {
        super(message, node);
        this.type = type;
    }
}
