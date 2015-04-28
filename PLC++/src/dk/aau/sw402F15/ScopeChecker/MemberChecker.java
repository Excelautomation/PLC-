package dk.aau.sw402F15.ScopeChecker;

import dk.aau.sw402F15.Exception.CompilerException;
import dk.aau.sw402F15.Exception.CompilerInternalException;
import dk.aau.sw402F15.Exception.MemberExpressionNotValidScopeCheckerException;
import dk.aau.sw402F15.Symboltable.*;
import dk.aau.sw402F15.Symboltable.Type.SymbolType;
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

    public Symbol getSymbol() {
        return currentSymbol;
    }

    @Override
    public void caseTIdentifier(TIdentifier node) {
        super.caseTIdentifier(node);

        if (currentSymbol == null)
        {
            currentSymbol = currentScope.getSymbolOrThrow(node.getText());

            // TODO arrays not handled
            if (currentSymbol.getType().getType() == SymbolType.Type.Function) {
                SymbolFunction symbolFunction = (SymbolFunction)currentSymbol;

                // Check if returntype is correct
                if (symbolFunction.getReturnType().getType() != SymbolType.Type.Struct) {
                    throw new NotImplementedException();
                }

                currentSymbol = currentScope.getSymbolOrThrow(symbolFunction.getReturnType().getName());
            }
            else if (currentSymbol.getClass() == SymbolVariable.class) {
                SymbolVariable symbolVariable = (SymbolVariable)currentSymbol;

                // Check if decltype is correct
                if (symbolVariable.getType().getType() != SymbolType.Type.Struct) {
                    throw new NotImplementedException();
                }

                currentSymbol = currentScope.getSymbolOrThrow(symbolVariable.getType().getName());
            }

            // Update scope if symbol is a struct
            if (currentSymbol.getType().getType() == SymbolType.Type.Struct) {
                currentScope = currentSymbol.getScope().getSubScopeByNodeOrThrow(currentSymbol.getNode());
                return;
            }
            else
                throw new MemberExpressionNotValidScopeCheckerException("Invalid node in member expr");
        }

        currentSymbol = currentScope.getSymbolOrThrow(node.getText());

        // Check if it's a function
        if (currentSymbol.getType().getType() == SymbolType.Type.Function) {
            SymbolFunction symbolFunction = (SymbolFunction)currentSymbol;

            // Check if returntype is correct
            if (symbolFunction.getReturnType().getType() == SymbolType.Type.Struct) {
                currentSymbol = currentScope.getSymbolOrThrow(symbolFunction.getReturnType().getName());
            }
        }

        // Update scope if symbol is a struct
        if (currentSymbol.getType().getType() == SymbolType.Type.Struct) {
            currentScope = currentSymbol.getScope().getSubScopeByNodeOrThrow(currentSymbol.getNode());
            return;
        }
        else {
            System.out.println("PANIC! : "  + currentSymbol.getName());
            //throw new MemberExpressionNotValidScopeCheckerException("Invalid node in member expr");
        }
    }

    /*
    @Override
    public void outAIdentifierExpr(AIdentifierExpr node) {
        super.outAIdentifierExpr(node);

        // If current symbol is not set we should just update
        if (currentSymbol == null) {
            updateSymbol(node.getName());
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
            throw new MemberExpressionNotValidScopeCheckerException("Symbol does not exists");
        } else if (currentSymbol.getClass() == SymbolVariable.class) {
            SymbolVariable variable = (SymbolVariable) currentSymbol;

            if (variable.getType().getType() != SymbolType.Type.Struct) {
                currentSymbol = variable;
                return;
            }

            // If variable is a struct
            ADeclaration declaration = (ADeclaration)variable.getNode();
            //updateSymbol(declaration.getType());
            throw new NotImplementedException();
        }

        throw new MemberExpressionNotValidScopeCheckerException("Invalid node in member expr");
    }

    @Override
    public void outAFunctionCallExpr(AFunctionCallExpr node) {
        super.outAFunctionCallExpr(node);

        if (currentSymbol == null) {
            updateSymbol(node.getName());

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
            throw new MemberExpressionNotValidScopeCheckerException("Symbol does not exists");
        } else
            throw new MemberExpressionNotValidScopeCheckerException("Invalid node in member expr");
    }*/

    private void updateSymbol(TIdentifier node) {
        currentSymbol = currentScope.getSymbolOrThrow(node.getText());

        // Check if symbol if a variable - if it is - find the type
        if (currentSymbol.getClass() == SymbolVariable.class) {
            SymbolVariable variable = (SymbolVariable) currentSymbol;
            if (variable.getNode().getClass() == ADeclaration.class) {
                ADeclaration declaration = (ADeclaration) variable.getNode();

                // Check if the variable is declared as a struct
                if (declaration.getType().getClass() != AStructTypeSpecifier.class) {
                    throw new MemberExpressionNotValidScopeCheckerException("Invalid node in member expr");
                }

                // Update currentSymbol and currentStruct to the found struct
                updateSymbolFromStructSpecifier((AStructTypeSpecifier) declaration.getType());

                return;
            }
        } else if (currentSymbol.getClass() == SymbolFunction.class) {
            SymbolFunction function = (SymbolFunction)currentSymbol;
            if (function.getNode().getClass() == AFunctionRootDeclaration.class) {
                AFunctionRootDeclaration functionNode = (AFunctionRootDeclaration) function.getNode();

                if (functionNode.getReturnType().getClass() != AStructTypeSpecifier.class) {
                    throw new MemberExpressionNotValidScopeCheckerException("Invalid node in member expr");
                }

                updateSymbolFromStructSpecifier((AStructTypeSpecifier) functionNode.getReturnType());

                return;
            }
        }

        throw new MemberExpressionNotValidScopeCheckerException("Invalid node in member expr");
    }

    private void updateSymbolFromStructSpecifier(AStructTypeSpecifier struct) {
        // Update currentSymbol
        currentSymbol = currentScope.getSymbolOrThrow(struct.getIdentifier().getText());

        // Update currentScope
        currentScope = currentSymbol.getScope().getSubScopeByNodeOrThrow(currentSymbol.getNode());
    }

}
