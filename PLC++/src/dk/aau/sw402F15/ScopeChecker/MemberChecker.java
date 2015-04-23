package dk.aau.sw402F15.ScopeChecker;

import dk.aau.sw402F15.Exception.CompilerException;
import dk.aau.sw402F15.Exception.CompilerInternalException;
import dk.aau.sw402F15.Symboltable.*;
import dk.aau.sw402F15.parser.analysis.DepthFirstAdapter;
import dk.aau.sw402F15.parser.node.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

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
            for (Symbol symbol : struct.getSymbolList()) {
                if (symbol.getName().equals(node.getName().getText())) {
                    currentSymbol = symbol;
                    currentScope = symbol.getScope();
                    return;
                }
            }

            // If symbol exists update symbol if not throw exception
            throw new CompilerInternalException("Fejl");
        }

    }


    private void updateSymbol(AIdentifierExpr node) {
        currentSymbol = currentScope.getSymbolOrThrow(node.getName().getText());

        if (currentSymbol.getClass() == SymbolVariable.class) {
            SymbolVariable variable = (SymbolVariable) currentSymbol;
            if (variable.getNode().getClass() == ADeclaration.class) {
                ADeclaration declaration = (ADeclaration) variable.getNode();

                if (declaration.getType().getClass() != AStructTypeSpecifier.class) {
                    throw new NotImplementedException();
                }

                AStructTypeSpecifier struct = (AStructTypeSpecifier) declaration.getType();
                currentSymbol = currentScope.getSymbolOrThrow(struct.getIdentifier().getText());

                // Update currentScope
                currentScope = currentSymbol.getScope().getSubScopeByNodeOrThrow(currentSymbol.getNode());
            } else if (variable.getNode().getClass() == AAssignmentDeclaration.class) {
                AAssignmentDeclaration declaration = (AAssignmentDeclaration) variable.getNode();

                if (declaration.getType().getClass() != AStructTypeSpecifier.class) {
                    throw new NotImplementedException();
                }

                AStructTypeSpecifier struct = (AStructTypeSpecifier) declaration.getType();
                currentSymbol = currentScope.getSymbolOrThrow(struct.getIdentifier().getText());

                // Update currentScope
                currentScope = currentScope.getSubScopeByNodeOrThrow(currentSymbol.getNode());
            }
            else throw new NotImplementedException();
        } else if (currentSymbol.getClass() == SymbolFunction.class) {
            throw new NotImplementedException();
        }
    }

}