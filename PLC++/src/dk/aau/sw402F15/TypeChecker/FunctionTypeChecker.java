package dk.aau.sw402F15.TypeChecker;

import dk.aau.sw402F15.Exception.TypeChecker.*;
import dk.aau.sw402F15.Symboltable.Scope;
import dk.aau.sw402F15.Symboltable.ScopeDepthFirstAdapter;
import dk.aau.sw402F15.Symboltable.Type.SymbolType;
import dk.aau.sw402F15.parser.node.*;

/**
 * Created by sahb on 27/04/15.
 */
public class FunctionTypeChecker extends ScopeDepthFirstAdapter {
    private final boolean hasReturnType;
    private final SymbolType returnType;

    private boolean returnStatementFound = false;

    public boolean returnStatementFound() {
        return returnStatementFound;
    }

    public FunctionTypeChecker(Scope rootScope, Scope currentScope, boolean hasReturnType, SymbolType returnType) {
        super(rootScope, currentScope);

        this.hasReturnType = hasReturnType;
        this.returnType = returnType;
    }

    @Override
    public void outAFunctionRootDeclaration(AFunctionRootDeclaration node) {
        super.outAFunctionRootDeclaration(node);

        // Check if function should return and if true check if we did not have a return and therefore should return an error
        if (hasReturnType && !returnStatementFound) {
            throw new MissingReturnStatementException(node);
        }
    }

    @Override
    public void caseADeclaration(ADeclaration node) {
        DeclarationTypeChecker declarationTypeChecker = new DeclarationTypeChecker(currentScope);
        node.apply(declarationTypeChecker);
    }

    @Override
    public void caseABranchStatement(ABranchStatement node) {
        // Check if condition is boolean
        if (node.getCondition() != null) {
            ExpressionTypeEvaluator expressionTypeEvaluator = new ExpressionTypeEvaluator(currentScope);
            node.getCondition().apply(expressionTypeEvaluator);

            SymbolType exprResult = expressionTypeEvaluator.getResult();
            if (!exprResult.equals(SymbolType.Type.Boolean)) {
                throw new ExpectingBoolException(node, exprResult);
            }
        }

        if(node.getCondition() != null)
        {
            node.getCondition().apply(this);
        }

        FunctionTypeChecker functionTypeChecker = new FunctionTypeChecker(rootScope, currentScope, hasReturnType, returnType),
                            functionTypeChecker1 = new FunctionTypeChecker(rootScope, currentScope, hasReturnType, returnType);
        if(node.getLeft() != null)
        {
            node.getLeft().apply(functionTypeChecker);
        }
        if(node.getRight() != null)
        {
            node.getRight().apply(functionTypeChecker1);
        }

        if (functionTypeChecker.returnStatementFound() && functionTypeChecker1.returnStatementFound()) {
            returnStatementFound = true;
        }
    }

    @Override
    public void caseAWhileStatement(AWhileStatement node) {
        // Check if condition is boolean
        if (node.getCondition() != null) {
            ExpressionTypeEvaluator expressionTypeEvaluator = new ExpressionTypeEvaluator(currentScope);
            node.getCondition().apply(expressionTypeEvaluator);

            SymbolType exprResult = expressionTypeEvaluator.getResult();
            if (!exprResult.equals(SymbolType.Type.Boolean)) {
                throw new ExpectingBoolException(node, exprResult);
            }
        }

        super.caseAWhileStatement(node);
    }

    @Override
    public void caseAForStatement(AForStatement node) {
        // Check if condition is boolean
        if (node.getCondition() != null) {
            ExpressionTypeEvaluator expressionTypeEvaluator = new ExpressionTypeEvaluator(currentScope);
            node.getCondition().apply(expressionTypeEvaluator);

            SymbolType exprResult = expressionTypeEvaluator.getResult();
            if (!exprResult.equals(SymbolType.Type.Boolean)) {
                throw new ExpectingBoolException(node, exprResult);
            }
        }

        super.caseAForStatement(node);
    }

    @Override
    public void caseAExprStatement(AExprStatement node) {
        ExpressionTypeEvaluator expressionTypeEvaluator = new ExpressionTypeEvaluator(currentScope);
        node.apply(expressionTypeEvaluator);
    }

    @Override
    public void caseAReturnStatement(AReturnStatement node) {
        super.caseAReturnStatement(node);

        if (node.getExpr() == null) {
            // Return without result

            // Check if the function has a returntype
            if (hasReturnType)
                throw new ReturnContainsNoExprException(node);
        } else {
            // Return with result

            // Check if function does not have a returntype
            if (!hasReturnType)
                throw new ReturnExprInVoidException(node);

            // Check if expr has correct return type
            ExpressionTypeEvaluator expressionTypeEvaluator = new ExpressionTypeEvaluator(currentScope);
            node.getExpr().apply(expressionTypeEvaluator);

            if (!returnType.equals(expressionTypeEvaluator.getResult()))
                throw new IncompatibleReturnTypeException(node, returnType);
        }

        returnStatementFound = true;
    }
}
