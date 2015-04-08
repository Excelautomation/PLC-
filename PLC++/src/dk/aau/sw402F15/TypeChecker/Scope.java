package dk.aau.sw402F15.TypeChecker;

import dk.aau.sw402F15.TypeChecker.Exceptions.SymbolAlreadyExists;
import dk.aau.sw402F15.TypeChecker.Exceptions.SymbolNotFoundException;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class Scope {
    private Dictionary<String, Symbol> symbols;
    private List<Scope> scopes;
    private Scope parentScope;

    public Scope(Scope parentScope) {
        this.symbols = new Hashtable<String, Symbol>();
        this.scopes = new ArrayList<Scope>();
        this.parentScope = parentScope;
    }

    public void addSymbol(String name, Type type) {
        if (getSymbol(name) != null) {
            throw new SymbolAlreadyExists();
        }

        Symbol symbol = new Symbol(name, this, type);
        symbols.put(name, symbol);
    }

    public Scope addSubScope() {
        Scope scope = new Scope(this);
        scopes.add(scope);
        return scope;
    }

    public Type getType(String name) {
        Symbol symbol = getSymbol(name);
        if (symbol != null)
            return symbol.type;

        return null;
    }

    public Type getTypeOrThrow(String name) {
        Type type = getType(name);

        if (type != null)
            return type;

        throw new SymbolNotFoundException();
    }

    public Scope getParentScope() {
        return parentScope;
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

    private class Symbol {
        private String name;
        private Scope scope;
        private Type type;

        public Symbol(String name, Scope scope, Type type) {
            this.name = name;
            this.scope = scope;
            this.type = type;
        }
    }
}
