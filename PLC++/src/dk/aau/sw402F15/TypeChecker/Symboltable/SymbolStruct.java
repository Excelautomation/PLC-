package dk.aau.sw402F15.TypeChecker.Symboltable;

import dk.aau.sw402F15.parser.node.Node;

public class SymbolStruct extends  Symbol {

    public SymbolStruct(String name, Node node, Scope scope) {
        super(SymbolType.Struct, name, node, scope);
    }
}
