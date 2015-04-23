package dk.aau.sw402F15.Symboltable.Table;

import dk.aau.sw402F15.parser.node.Node;

/**
 * Created by sahb on 22/04/15.
 */
public abstract class Symbol {
    private final Node node;
    private final String name;

    public Symbol(Node node) {
        this(node, null);
    }

    public Symbol(Node node, String name) {
        this.node = node;
        this.name = name;
    }

    public boolean hasName() {
        return name != null;
    }

    public Node getNode() {
        return node;
    }

    public String getName() {
        return name;
    }
}

