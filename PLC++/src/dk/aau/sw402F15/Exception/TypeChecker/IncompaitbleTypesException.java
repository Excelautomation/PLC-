package dk.aau.sw402F15.Exception.TypeChecker;

import dk.aau.sw402F15.Symboltable.Type.SymbolType;
import dk.aau.sw402F15.parser.node.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sahb on 07/05/15.
 */
public class IncompaitbleTypesException extends TypeCheckerException {
    private List<SymbolType> types;

    private IncompaitbleTypesException(Node node, SymbolType type1) {
        super(node);
        this.types = new ArrayList<SymbolType>();

        types.add(type1);
    }

    public IncompaitbleTypesException(Node node, SymbolType type1, SymbolType type2) {
        this(node, type1);

        types.add(type2);
    }

    public IncompaitbleTypesException(Node node, SymbolType type1, SymbolType type2, SymbolType type3) {
        this(node, type1, type2);

        types.add(type3);
    }
}