package dk.aau.sw402F15.Symboltable;

import dk.aau.sw402F15.Symboltable.Type.SymbolType;
import dk.aau.sw402F15.parser.node.Node;

public abstract class Symbol {
    private SymbolType type;
    private String name;
    private Scope scope;
    private Node node;

    public Symbol(SymbolType type, String name, Node node, Scope scope) {
        this.type = type;
        this.name = name;
        this.scope = scope;
        this.node = node;
    }

    public Node getNode() {
        return node;
    }

    public Scope getScope() {
        return scope;
    }

    public String getName() {
        return name;
    }

    public SymbolType getType() {
        return type;
    }
}

