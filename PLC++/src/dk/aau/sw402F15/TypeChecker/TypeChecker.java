package dk.aau.sw402F15.TypeChecker;

import dk.aau.sw402F15.TypeChecker.Exceptions.*;
import dk.aau.sw402F15.TypeChecker.Symboltable.Scope;
import dk.aau.sw402F15.TypeChecker.Symboltable.Symbol;
import dk.aau.sw402F15.TypeChecker.Symboltable.SymbolFunction;
import dk.aau.sw402F15.TypeChecker.Symboltable.SymbolType;
import dk.aau.sw402F15.parser.analysis.DepthFirstAdapter;
import dk.aau.sw402F15.parser.node.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

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
            throw new IllegalAssignment();
        }
    }

    @Override
    public void inAFunctionRootDeclaration(AFunctionRootDeclaration node) {
        super.inAFunctionRootDeclaration(node);

        stack.push(((SymbolFunction)currentScope.getSymbol(node.getName().getText())).getReturnType());
    }

    @Override
    public void outAFunctionRootDeclaration(AFunctionRootDeclaration node) {
        super.outAFunctionRootDeclaration(node);

        stack.pop();

        if (node.getReturnType().getClass() != AVoidTypeSpecifier.class && !returnFound)
            throw new MissingReturnStatement();
        if (node.getReturnType().getClass() == AVoidTypeSpecifier.class && returnFound)
            throw new ReturnInVoidFunction();
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
        SymbolType returnType = stack.peek();


    }
}