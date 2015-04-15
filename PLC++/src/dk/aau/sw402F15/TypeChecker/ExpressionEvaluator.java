package dk.aau.sw402F15.TypeChecker;

import dk.aau.sw402F15.TypeChecker.Exceptions.IllegalAssignment;
import dk.aau.sw402F15.TypeChecker.Exceptions.IllegalComparison;
import dk.aau.sw402F15.TypeChecker.Exceptions.IllegalExpression;
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
 * Created by Mikkel on 15-04-2015.
 */
public class ExpressionEvaluator extends DepthFirstAdapter {
    protected final Scope rootScope;
    protected Scope currentScope;
    protected Stack<SymbolType> stack = new Stack<SymbolType>();

    public ExpressionEvaluator(Scope rootScope, Scope currentScope) {
        this.rootScope = rootScope;
        this.currentScope = currentScope;
    }

    public SymbolType getSymbol() {
        return stack.pop();
    }

    @Override
    public void inAScopeStatement(AScopeStatement node) {
        super.inAScopeStatement(node);
        currentScope = currentScope.getSubScopeByNodeOrThrow(node);
    }

    @Override
    public void outAScopeStatement(AScopeStatement node) {
        super.outAScopeStatement(node);
        currentScope = currentScope.getParentScope();
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
                throw new RuntimeException();

            // Check each expression
            for (int i = 0; i < copy.size(); i++) {
                PExpr e = copy.get(i);

                // Get expression type
                e.apply(expressionEvaluator);

                SymbolType type = expressionEvaluator.getSymbol();
                if (type != func.getFormalParameters().get(i))
                    throw new RuntimeException();
            }
        }

        // Push return type to stack
        stack.push(func.getReturnType());
    }

    @Override
    public void outAAssignmentExpr(AAssignmentExpr node) {
        super.outAAssignmentExpr(node);

        SymbolType arg1 = currentScope.getSymbol(node.getName().getText()).getType();
        SymbolType arg2 = stack.pop();

        if (arg1 != arg2) {
            throw new IllegalAssignment();
        }
    }

    @Override
    public void outAIdentifierExpr(AIdentifierExpr node) {
        super.outAIdentifierExpr(node);

        stack.push(currentScope.getSymbol(node.getName().getText()).getType());
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
            throw new IllegalComparison();
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
            throw new IllegalExpression();
        }
    }
}