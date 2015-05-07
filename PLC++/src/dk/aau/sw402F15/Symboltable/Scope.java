package dk.aau.sw402F15.Symboltable;

import dk.aau.sw402F15.Exception.SymbolTable.ScopeNotFoundException;
import dk.aau.sw402F15.Exception.SymbolTable.SymbolAlreadyExistsException;
import dk.aau.sw402F15.Exception.SymbolTable.SymbolNotFoundException;
import dk.aau.sw402F15.parser.node.Node;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

public class Scope {
    private Hashtable<String, Symbol> symbols;
    private ArrayList<Scope> scopes;
    private Scope parentScope;
    private Node node;

    public Scope(Scope parentScope, Node node) {
        this.node = node;
        this.symbols = new Hashtable<String, Symbol>();
        this.scopes = new ArrayList<Scope>();
        this.parentScope = parentScope;
    }

    public List<Symbol> toList() {
        Enumeration<Symbol> enumeration = symbols.elements();
        List<Symbol> result = new ArrayList<Symbol>(symbols.size());
        while (enumeration.hasMoreElements()) {
            Symbol symbol = enumeration.nextElement();
            result.add(symbol);
        }
        return result;
    }

    public void addSymbol(Symbol symbol) {
        if (getSymbol(symbol.getName()) != null) {
            throw new SymbolAlreadyExistsException(symbol.getNode());
        }

        symbols.put(symbol.getName(), symbol);
    }

    public Scope addSubScope(Node node) {
        Scope scope = new Scope(this, node);
        scopes.add(scope);
        return scope;
    }

    private Symbol getSymbol(String name) {
        Symbol symbol = symbols.get(name);
        if (symbol != null)
            return symbol;

        Scope scope = getParentScope();
        if (scope != null)
            return scope.getSymbol(name);

        return null;
    }

    public Symbol getSymbolOrThrow(String name, Node node) {
        Symbol symbol = getSymbol(name);

        if (symbol == null)
            throw new SymbolNotFoundException(node);

        return symbol;
    }

    public Scope getSubScopeByNode(Node node) {
        for (Scope scope : scopes) {
            if (scope.getNode() == node)
                return scope;
        }

        return null;
    }

    public Scope getSubScopeByNodeOrThrow(Node node) {
        Scope scope = getSubScopeByNode(node);

        if (scope == null)
            throw new ScopeNotFoundException(node);

        return scope;
    }

    public Scope getParentScope() {
        return parentScope;
    }

    public Node getNode() {
        return node;
    }

    public void clearSymbols() {
        this.symbols.clear();
    }
}
