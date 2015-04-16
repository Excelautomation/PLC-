package dk.aau.sw402F15.TypeChecker;

import dk.aau.sw402F15.TypeChecker.Exceptions.*;
import dk.aau.sw402F15.TypeChecker.Symboltable.Scope;
import dk.aau.sw402F15.TypeChecker.Symboltable.SymbolFunction;
import dk.aau.sw402F15.TypeChecker.Symboltable.SymbolType;
import dk.aau.sw402F15.parser.node.*;

/**
 * Created by Mikkel on 08-04-2015.
 */
public class TypeChecker extends ExpressionEvaluator {
    public TypeChecker(Scope rootScope) {
        super(rootScope, rootScope);
    }

    private boolean returnFound = false;

    @Override
    public void outAAssignmentDeclaration(AAssignmentDeclaration node) {
        super.outAAssignmentDeclaration(node);

        SymbolType arg1 = currentScope.getSymbol(node.getName().getText()).getType();
        SymbolType arg2 = stack.pop();

        if (arg1 != arg2) {
            throw new IllegalAssignmentException();
        }
    }

    @Override
    public void inAFunctionRootDeclaration(AFunctionRootDeclaration node) {
        super.inAFunctionRootDeclaration(node);

        stack.push(((SymbolFunction) currentScope.getSymbol(node.getName().getText())).getReturnType());
    }

    @Override
    public void outAFunctionRootDeclaration(AFunctionRootDeclaration node) {
        super.outAFunctionRootDeclaration(node);

        stack.pop();

        if (node.getReturnType().getClass() != AVoidTypeSpecifier.class && !returnFound)
            throw new MissingReturnStatementException();
        if (node.getReturnType().getClass() == AVoidTypeSpecifier.class && returnFound)
            throw new ReturnInVoidFunctionException();
    }



    @Override
    public void inAReturnStatement(AReturnStatement node) {
        returnFound = true;
        super.outAReturnStatement(node);
    }

    @Override
    public void outAReturnExprStatement(AReturnExprStatement node) {
        super.outAReturnExprStatement(node);

        returnFound = true;
        SymbolType arg1 = stack.pop();
        SymbolType returnType = stack.peek();

        if (returnType != arg1) {
            throw new IllegalReturnTypeException();
        }

    }
}