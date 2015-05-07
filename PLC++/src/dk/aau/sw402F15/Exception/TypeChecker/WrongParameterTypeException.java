package dk.aau.sw402F15.Exception.TypeChecker;

import dk.aau.sw402F15.Symboltable.Type.SymbolType;
import dk.aau.sw402F15.parser.node.Node;

/**
 * Created by sahb on 07/05/15.
 */
public class WrongParameterTypeException extends TypeCheckerException {
    private SymbolType expectedType;
    private SymbolType actualType;

    public WrongParameterTypeException(Node node, SymbolType expectedType, SymbolType actualType) {
        super("Wrong actual parameter " +
                "(expected: " + expectedType.toString() + "; " +
                "actual: " + actualType.toString() + ")", node);
        this.expectedType = expectedType;
        this.actualType = actualType;
    }

    public WrongParameterTypeException(String message, Node node, SymbolType expectedType, SymbolType actualType) {
        super(message, node);
        this.expectedType = expectedType;
        this.actualType = actualType;
    }
}
