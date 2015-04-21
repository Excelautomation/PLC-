package dk.aau.sw402F15.TypeChecker;
import dk.aau.sw402F15.TypeChecker.Exceptions.*;
import dk.aau.sw402F15.TypeChecker.Symboltable.*;
import dk.aau.sw402F15.parser.node.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by Mikkel on 15-04-2015.
 */
public class ExpressionEvaluator extends ScopeDepthFirstAdapter {
    protected Stack<SymbolType> stack = new Stack<SymbolType>();

    public ExpressionEvaluator(Scope rootScope, Scope currentScope) {
        super(rootScope, currentScope);
    }

    public SymbolType getSymbol() {
        return stack.pop();
    }

    @Override
    public void caseAFunctionCallExpr(AFunctionCallExpr node) {
        Symbol symbol = currentScope.getSymbol(node.getName().getText());
        SymbolFunction func;

        // Check if type is a function
        if (symbol.getClass() == SymbolFunction.class) {
            func = (SymbolFunction) symbol;
        } else {
            throw new RuntimeException();
        }

        // Check arguments
        {
            ExpressionEvaluator expressionEvaluator = new ExpressionEvaluator(rootScope, currentScope);
            List<PExpr> copy = new ArrayList<PExpr>(node.getArgs());

            // Check number of parameters
            if (copy.size() != func.getFormalParameters().size())
                throw new WrongParameterException();

            // Check each expression
            for (int i = 0; i < copy.size(); i++) {
                PExpr e = copy.get(i);

                // Get expression type
                e.apply(expressionEvaluator);

                SymbolType type = expressionEvaluator.getSymbol();
                if (type != func.getFormalParameters().get(i))
                    throw new WrongParameterException();
            }
        }

        // Push return type to stack
        stack.push(func.getReturnType());
    }

    @Override
    public void caseAAssignmentExpr(AAssignmentExpr node) {
        inAAssignmentExpr(node);

        if (node.getLeft().getClass() == AArrayExpr.class)
        {
            Symbol symbol = currentScope.getSymbolOrThrow(((AArrayExpr)node.getLeft()).getName().getText());
            stack.push(((SymbolArray) symbol).getContainedType());
        }
        stack.peek();

        if(node.getLeft() != null && !(node.getLeft().getClass() == AArrayExpr.class))
        {
            ExpressionEvaluatorWithConst evaluator = new ExpressionEvaluatorWithConst(rootScope, currentScope);
            node.getLeft().apply(evaluator);

            stack.push(evaluator.getSymbol());
        }
        if(node.getRight() != null)
        {
            node.getRight().apply(this);
            stack.push(this.getSymbol());
        }
        outAAssignmentExpr(node);
    }

    @Override
    public void outAAssignmentExpr(AAssignmentExpr node) {
        super.outAAssignmentExpr(node);

        SymbolType arg1 = stack.pop();
        SymbolType arg2 = stack.pop();

        stack.peek();
        if (arg1 != arg2) {
            throw new IllegalAssignmentException();
        }
    }

    @Override
    public void outAIdentifierExpr(AIdentifierExpr node) {
        super.outAIdentifierExpr(node);

        stack.push(currentScope.getSymbolOrThrow(node.getName().getText()).getType());
    }

    @Override
    public void outAIntegerExpr(AIntegerExpr node) {
        super.outAIntegerExpr(node);
        stack.push(SymbolType.Int);
    }

    @Override
    public void outADecimalExpr(ADecimalExpr node) {
        super.outADecimalExpr(node);
        stack.push(SymbolType.Decimal);
    }

    @Override
    public void outATrueExpr(ATrueExpr node) {
        super.outATrueExpr(node);
        stack.push(SymbolType.Boolean);
    }

    @Override
    public void outAFalseExpr(AFalseExpr node) {
        super.outAFalseExpr(node);
        stack.push(SymbolType.Boolean);
    }

    @Override
    public void outACompareGreaterExpr(ACompareGreaterExpr node) {
        super.outACompareGreaterExpr(node);
        checkComparison();
    }

    @Override
    public void outACompareLessExpr(ACompareLessExpr node) {
        super.outACompareLessExpr(node);
        checkComparison();
    }

    @Override
    public void outACompareLessOrEqualExpr(ACompareLessOrEqualExpr node) {
        super.outACompareLessOrEqualExpr(node);
        checkComparison();
    }

    @Override
    public void outACompareGreaterOrEqualExpr(ACompareGreaterOrEqualExpr node) {
        super.outACompareGreaterOrEqualExpr(node);
        checkComparison();
    }

    @Override
    public void outACompareEqualExpr(ACompareEqualExpr node) {
        super.outACompareEqualExpr(node);
        checkComparison();
    }

    @Override
    public void outACompareNotEqualExpr(ACompareNotEqualExpr node) {
        super.outACompareNotEqualExpr(node);
        checkComparison();
    }

    @Override
    public void outAAddExpr(AAddExpr node) {
        super.outAAddExpr(node);
        checkExpression();
    }

    @Override
    public void outASubExpr(ASubExpr node) {
        super.outASubExpr(node);
        checkExpression();
    }

    @Override
    public void outAMultiExpr(AMultiExpr node) {
        super.outAMultiExpr(node);
        checkExpression();
    }

    @Override
    public void outADivExpr(ADivExpr node) {
        super.outADivExpr(node);
        checkExpression();

        // Checking that we don't divide by zero
        if (node.getRight() instanceof AIntegerExpr)
        {
            if (Integer.parseInt(((AIntegerExpr) node.getRight()).getIntegerLiteral().getText()) == 0)
                throw new DivisionByZeroException();
        }
        else if (node.getRight() instanceof ADecimalExpr)
        {
            if (Float.parseFloat(((ADecimalExpr) node.getRight()).getDecimalLiteral().getText()) == 0.0)
                throw new DivisionByZeroException();
        }
    }

    @Override
    public void outAModExpr(AModExpr node) {
        super.outAModExpr(node);
        checkExpression();
    }

    private void checkComparison() {
        SymbolType arg2 = stack.pop(), arg1 = stack.pop();

        if ((arg1 == SymbolType.Int && arg2 == SymbolType.Int) || (arg1 == SymbolType.Decimal && arg2 == SymbolType.Decimal)) {
            stack.push(SymbolType.Boolean);
        }
        else {
            throw new IllegalComparisonException();
        }
    }

    private void checkExpression(){
        SymbolType arg2 = stack.pop(), arg1 = stack.pop();

        if (arg1 == SymbolType.Int && arg2 == SymbolType.Int){
            stack.push(SymbolType.Int);
        }
        else if (arg1 == SymbolType.Decimal && arg2 == SymbolType.Decimal){
            stack.push(SymbolType.Decimal);
        }
        else{
            throw new IllegalExpressionException();
        }
    }
}
