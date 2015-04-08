package dk.aau.sw402F15.TypeChecker.Symboltable;

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

    public void addSymbol(Symbol symbol) {
        if (getSymbol(symbol.getName()) != null) {
            throw new SymbolAlreadyExists();
        }

        symbols.put(symbol.getName(), symbol);
    }

    public Scope addSubScope() {
        Scope scope = new Scope(this);
        scopes.add(scope);
        return scope;
    }

    public Symbol getSymbol(String name) {
        Symbol symbol = symbols.get(name);
        if (symbol != null)
            return symbol;

        Scope scope = getParentScope();
        if (scope != null)
            return scope.getSymbol(name);

        return null;
    }

    public Symbol getSymbolOrThrow(String name) {
        Symbol symbol = getSymbol(name);

        if (symbol == null)
            throw new SymbolNotFoundException();

        return symbol;
    }

    public Scope getParentScope() {
        return parentScope;
    }
}
