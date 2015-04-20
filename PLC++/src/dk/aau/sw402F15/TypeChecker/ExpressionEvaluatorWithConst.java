package dk.aau.sw402F15.TypeChecker;

import dk.aau.sw402F15.TypeChecker.Exceptions.RedefinitionOfReadOnlyException;
import dk.aau.sw402F15.TypeChecker.Symboltable.Scope;
import dk.aau.sw402F15.TypeChecker.Symboltable.Symbol;
import dk.aau.sw402F15.TypeChecker.Symboltable.SymbolType;
import dk.aau.sw402F15.TypeChecker.Symboltable.SymbolVariable;
import dk.aau.sw402F15.parser.node.AIdentifierExpr;

/**
 * Created by sahb on 20/04/15.
 */
public class ExpressionEvaluatorWithConst extends ExpressionEvaluator {
    public ExpressionEvaluatorWithConst(Scope rootScope, Scope currentScope) {
        super(rootScope, currentScope);
    }

    @Override
    public void outAIdentifierExpr(AIdentifierExpr node) {
        // Get symbol
        Symbol symbol = currentScope.getSymbolOrThrow(node.getName().getText());

        if (symbol.getClass() == SymbolVariable.class) {
            boolean isConst = ((SymbolVariable) symbol).isConst();

            if (isConst) {
                throw new RedefinitionOfReadOnlyException();
            }
        }

        stack.push(symbol.getType());
    }
}
