package dk.aau.sw402F15.Exception.TypeChecker;

import dk.aau.sw402F15.Symboltable.Type.SymbolType;
import dk.aau.sw402F15.parser.node.Node;

/**
 * Created by sahb on 07/05/15.
 */
public class IncompatibleReturnTypeException extends TypeCheckerException {
    private final SymbolType symbolType;

    public IncompatibleReturnTypeException(Node node, SymbolType symbolType) {
        this("Incompatible returntype: " + symbolType.toString(), node, symbolType);
    }

    public IncompatibleReturnTypeException(String message, Node node, SymbolType symbolType) {
        super(message, node);
        this.symbolType = symbolType;
    }
}

