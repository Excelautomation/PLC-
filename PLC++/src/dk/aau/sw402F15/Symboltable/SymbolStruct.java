package dk.aau.sw402F15.Symboltable;

import dk.aau.sw402F15.Symboltable.Type.SymbolType;
import dk.aau.sw402F15.parser.node.Node;

import java.util.List;

public class SymbolStruct extends  Symbol {

    private List<Symbol> symbolList;

    public SymbolStruct(String name, List<Symbol> symbolList, Node node, Scope scope) {
        super(SymbolType.Struct(name), name, node, scope);

        this.symbolList = symbolList;
    }

    public List<Symbol> getSymbolList() {
        return symbolList;
    }
}
