package dk.aau.sw402F15.TypeChecker.Symboltable;

import dk.aau.sw402F15.parser.node.Node;

/**
 * Created by Claus on 15-04-2015.
 */
public class SymbolArray extends Symbol {
    SymbolType containedType;

    public SymbolArray(SymbolType containedType, String name, Node node, Scope scope){
        super(SymbolType.Array, name, node, scope);
        this.containedType = containedType;
    }

    public SymbolType getContainedType(){
        return containedType;
    }
}
