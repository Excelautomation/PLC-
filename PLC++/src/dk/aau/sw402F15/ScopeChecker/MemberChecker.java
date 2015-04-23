package dk.aau.sw402F15.ScopeChecker;

import dk.aau.sw402F15.Exception.CompilerException;
import dk.aau.sw402F15.Exception.CompilerInternalException;
import dk.aau.sw402F15.Symboltable.*;
import dk.aau.sw402F15.parser.analysis.DepthFirstAdapter;
import dk.aau.sw402F15.parser.node.AIdentifierExpr;
import dk.aau.sw402F15.parser.node.AMemberExpr;

/**
 * Created by sahb on 23/04/15.
 */
public class MemberChecker extends DepthFirstAdapter {
    private Scope currentScope;
    private Symbol currentSymbol;


    public MemberChecker(Scope currentScope) {
        this.currentScope = currentScope;
    }

    @Override
    public void outAIdentifierExpr(AIdentifierExpr node) {
        super.outAIdentifierExpr(node);

        // If current symbol is not set we should just update
        if (currentSymbol == null) {
            updateSymbol(node);
            return;
        }

        // Check currentSymbol
        if (currentSymbol.getClass() == SymbolStruct.class) {
            SymbolStruct struct = (SymbolStruct) currentSymbol;

            // Check if symbol exists
            boolean foundSymbol = false;
            for (Symbol symbol : struct.getSymbolList()) {
                if (symbol.getName().equals(node.getName().getText()))
                    foundSymbol = true;
            }

            // If symbol exists update symbol if not throw exception
            if (foundSymbol)
                updateSymbol(node);
            else
                throw new CompilerInternalException("Fejl");
        }

    }


    private void updateSymbol(AIdentifierExpr node) {
        currentSymbol = currentScope.getSymbolOrThrow(node.getName().getText());

        if (currentSymbol.getClass() == SymbolVariable.class) {
            SymbolVariable variable = (SymbolVariable) currentSymbol;
            //variable.
        } else if (currentSymbol.getClass() == SymbolFunction.class) {

        }
    }

}
