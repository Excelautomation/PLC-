package dk.aau.sw402F15.Symboltable.Table;

import dk.aau.sw402F15.Exception.CompilerSymbolTableSymbolExcistsException;
import dk.aau.sw402F15.parser.node.Node;

import java.util.ArrayList;
import java.util.List;

public abstract class SymbolScope extends Symbol {
    private List<Symbol> symbols;
    private SymbolScope parentScope;

    public SymbolScope(Node node) {
        super(node);

        symbols = new ArrayList<Symbol>();
    }

    public void addSymbol(Symbol symbol) {
        if (hasSymbol(symbol.getName()))
            throw new CompilerSymbolTableSymbolExcistsException();

        symbols.add(symbol);
    }

    public boolean hasSymbol(String name) {
        // Check if symbol is in current scope
        for (Symbol symbol : symbols) {
            if (symbol.hasName() && symbol.getName().equals(name))
                return true;
        }

        // Check if we are in rootScope - then it's not possible to go one level up and the symbol is therefore not excisting in the reacheable scope
        if (parentScope == null)
            return false;

        return parentScope.hasSymbol(name);
    }

    public Symbol getSymbol(String name) {
        // Check if symbol is in current scope
        for (Symbol symbol : symbols) {
            if (symbol.hasName() && symbol.getName().equals(name))
                return symbol;
        }

        // Check if we are in rootScope - then it's not possible to go one level up and the symbol is therefore not excisting in the reacheable scope
        if (parentScope == null)
            return null;

        return parentScope.getSymbol(name);
    }

    public Symbol getSymbolFromNode(Node node) {
        // Check if node is in currentScope
        for (Symbol value : symbols) {
            if (value.getNode() == node)
                return this;
        }

        // Not found
        return null;
    }

    public Symbol getDeptSymbolFromNode(Node node) {
        // Check if node is in currentScope
        for (Symbol value : symbols) {
            if (value.getNode() == node)
                return this;

            if (value.getClass() == SymbolScope.class) {
                SymbolScope scope = (SymbolScope) value;

                // Check if we can find symbol in subscope
                Symbol outputSymbol = scope.getDeptSymbolFromNode(node);

                // If found symbol in subscope return it
                if (outputSymbol != null)
                    return outputSymbol;
            }
        }
    }

    public SymbolScope getParentScope() {
        return parentScope;
    }
}
