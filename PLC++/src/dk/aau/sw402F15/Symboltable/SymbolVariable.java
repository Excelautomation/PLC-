package dk.aau.sw402F15.Symboltable;

import dk.aau.sw402F15.parser.node.Node;

/**
 * Created by Mikkel on 16-04-2015.
 */
public class SymbolVariable extends Symbol {
    private boolean _isConst;

    public SymbolVariable(SymbolType type, String name, Node node, Scope scope, boolean isConst) {
        super(type, name, node, scope);
        this._isConst = isConst;
    }

    public boolean isConst(){
        return _isConst;
    }
}
