package dk.aau.sw402F15.TypeChecker;

import dk.aau.sw402F15.Helper;
import dk.aau.sw402F15.Symboltable.Scope;
import dk.aau.sw402F15.Symboltable.ScopeDepthFirstAdapter;
import dk.aau.sw402F15.Symboltable.Type.SymbolType;
import dk.aau.sw402F15.TypeChecker.Exceptions.IllegalAssignmentException;
import dk.aau.sw402F15.TypeChecker.Exceptions.IllegalReturnTypeException;
import dk.aau.sw402F15.TypeChecker.Exceptions.MissingReturnStatementException;
import dk.aau.sw402F15.parser.node.*;

/**
 * Created by sahb on 27/04/15.
 */
public class FunctionTypeChecker extends ScopeDepthFirstAdapter {
    private final boolean hasReturnType;
    private final SymbolType returnType;

    private boolean returnStatementFound = false;

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
            throw new MissingReturnStatementException();
        }
    }

    @Override
    public void caseADeclaration(ADeclaration node) {
        // Check if declaration is not assigned
        if (node.getExpr() == null)
            return;

        ExpressionTypeEvaluator expressionTypeEvaluator = new ExpressionTypeEvaluator(currentScope);
        node.apply(expressionTypeEvaluator);

        // Get expr type
        SymbolType exprType = expressionTypeEvaluator.getResult();

        // Check type of declaration
        SymbolType declarationType = Helper.getSymbolTypeFromTypeSpecifier(node.getType());

        // Check if we must make a implicit type conversion
        if (exprType.getType() == SymbolType.Type.Int && declarationType.getType() == SymbolType.Type.Decimal) {
            exprType = SymbolType.Decimal();
        }

        // Check if types matches
        if (exprType.getType() != declarationType.getType()) {
            throw new IllegalAssignmentException();
        }
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
                throw new IllegalReturnTypeException();
        } else {
            // Return with result

            // Check if function does not have a returntype
            if (!hasReturnType)
                throw new IllegalReturnTypeException();

            // Check if expr has correct return type
            ExpressionTypeEvaluator expressionTypeEvaluator = new ExpressionTypeEvaluator(currentScope);
            node.getExpr().apply(expressionTypeEvaluator);

            if (returnType.getType() != expressionTypeEvaluator.getResult().getType())
                throw new IllegalReturnTypeException();
        }

        returnStatementFound = true;
    }
}
