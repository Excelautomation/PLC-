package dk.aau.sw402F15.TypeChecker.Symboltable;

import dk.aau.sw402F15.parser.node.Node;

/**
 * Created by Claus on 16-04-2015.
 */
public class SymbolEnum extends Symbol{
    public SymbolEnum(String name, Node node, Scope scope) {
        super(SymbolType.Enum, name, node, scope);
    }
}
