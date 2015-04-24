package dk.aau.sw402F15.TypeChecker;

import dk.aau.sw402F15.TypeChecker.Exceptions.*;
import dk.aau.sw402F15.Symboltable.*;
import dk.aau.sw402F15.parser.node.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mikkel on 08-04-2015.
 */
public class TypeChecker extends ExpressionEvaluator {
    public TypeChecker(Scope rootScope) {
        super(rootScope, rootScope);
    }

    // When we enter a function, we need to know if we've encountered a return statement
    private boolean returnFound = false;

    @Override
    public void outADeclaration(ADeclaration node) {
        super.outADeclaration(node);

        // Check if declaration contains an assignment
        if (node.getExpr() != null) {
            SymbolType arg1 = currentScope.getSymbol(node.getName().getText()).getType();
            SymbolType arg2 = stack.pop();

            if (arg1 != arg2) {
                throw new IllegalAssignmentException();
            }
        }
    }

    @Override
    public void outAWhileStatement(AWhileStatement node) {
        super.outAWhileStatement(node);

        // Checking that the while loop's condition is a boolean
        if (stack.pop() != SymbolType.Boolean)
            throw new IllegalLoopConditionException();
    }

    @Override
    public void caseAForStatement(AForStatement node) {
        super.caseAForStatement(node);

        // Checking that the for loop's condition is a boolean
        if (stack.pop() != SymbolType.Boolean)
            throw new IllegalLoopConditionException();
    }

    @Override
    public void outAArrayDefinition(AArrayDefinition node) {
        super.outAArrayDefinition(node);
    }

    @Override
    public void inAFunctionRootDeclaration(AFunctionRootDeclaration node) {
        super.inAFunctionRootDeclaration(node);

        // Pushing a function's return type to the stack
        stack.push(((SymbolFunction) currentScope.getSymbol(node.getName().getText())).getReturnType());
    }

    @Override
    public void outAFunctionRootDeclaration(AFunctionRootDeclaration node) {
        super.outAFunctionRootDeclaration(node);

        stack.pop();

        // Making sure void doesn't contain return, and that non-void function does contain return
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

        SymbolType arg1 = stack.pop();

        if (node.getExpr() instanceof AArrayExpr)
        {
            arg1 = ((SymbolArray) currentScope.getSymbolOrThrow(((AArrayExpr)node.getExpr()).getName().getText())).getContainedType();
        }

        returnFound = true;

        SymbolType returnType = stack.peek();

        // Checking that function's returntype matches what we're returning
        if (returnType != arg1) {
            throw new IllegalReturnTypeException();
        }

    }

}