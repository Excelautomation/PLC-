package dk.aau.sw402F15.ScopeChecker;

import dk.aau.sw402F15.Exception.ScopeChecker.MemberExpressionIsNotAStructException;
import dk.aau.sw402F15.Symboltable.Scope;
import dk.aau.sw402F15.Symboltable.Symbol;
import dk.aau.sw402F15.Symboltable.SymbolFunction;
import dk.aau.sw402F15.Symboltable.SymbolVariable;
import dk.aau.sw402F15.Symboltable.Type.SymbolType;
import dk.aau.sw402F15.parser.analysis.DepthFirstAdapter;
import dk.aau.sw402F15.parser.node.TIdentifier;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by sahb on 23/04/15.
 */
public class MemberChecker extends DepthFirstAdapter {
    private Scope currentScope;
    private SymbolType currentSymbol;


    public MemberChecker(Scope currentScope) {
        this.currentScope = currentScope;
    }

    public SymbolType getSymbol() {
        return currentSymbol;
    }

    @Override
    public void caseTIdentifier(TIdentifier node) {
        super.caseTIdentifier(node);

        if (currentSymbol == null) {
            currentSymbol = currentScope.getSymbolOrThrow(node.getText(), node).getType();

            // Get symbol
            Symbol symbol = currentScope.getSymbolOrThrow(currentSymbol.getName(), node);

            // TODO arrays not handled
            if (currentSymbol.getType() == SymbolType.Type.Function) {
                SymbolFunction symbolFunction = (SymbolFunction) symbol;

                // Check if returntype is correct
                if (symbolFunction.getReturnType().getType() != SymbolType.Type.Struct) {
                    throw new NotImplementedException();
                }

                currentSymbol = currentScope.getSymbolOrThrow(symbolFunction.getReturnType().getName(), node).getType();
            } else if (symbol.getClass() == SymbolVariable.class) {
                SymbolVariable symbolVariable = (SymbolVariable) symbol;

                // Check if decltype is correct
                if (symbolVariable.getType().getType() != SymbolType.Type.Struct) {
                    throw new NotImplementedException();
                }

                currentSymbol = currentScope.getSymbolOrThrow(symbolVariable.getType().getName(), node).getType();
            }

            // Update scope if symbol is a struct
            if (currentSymbol.getType() == SymbolType.Type.Struct) {
                symbol = currentScope.getSymbolOrThrow(currentSymbol.getName(), node);
                currentScope = symbol.getScope().getSubScopeByNodeOrThrow(symbol.getNode());
                return;
            } else
                throw new MemberExpressionIsNotAStructException(node);
        }

        currentSymbol = currentScope.getSymbolOrThrow(node.getText(), node).getType();

        // Check if it's a function
        if (currentSymbol.getType() == SymbolType.Type.Function) {
            SymbolFunction symbolFunction = (SymbolFunction) currentScope.getSymbolOrThrow(currentSymbol.getName(), node);

            // Check if returntype is correct
            if (symbolFunction.getReturnType().getType() == SymbolType.Type.Struct) {
                currentSymbol = currentScope.getSymbolOrThrow(symbolFunction.getReturnType().getName(), node).getType();
            } else {
                // Returntype is not a struct just update currentSymbol
                currentSymbol = symbolFunction.getReturnType();
            }
        }

        // Update scope if symbol is a struct
        if (currentSymbol.getType() == SymbolType.Type.Struct) {
            Symbol symbol = currentScope.getSymbolOrThrow(currentSymbol.getName(), node);
            currentScope = symbol.getScope().getSubScopeByNodeOrThrow(symbol.getNode());
            return;
        } else {
            if (currentSymbol.hasName())
                System.out.println("MemberChecker found symbol which is not a struct or function: " + currentSymbol.getName());
        }
    }

}
