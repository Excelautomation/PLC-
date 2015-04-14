package dk.aau.sw402F15.TypeChecker;

import dk.aau.sw402F15.TypeChecker.Exceptions.IllegalAssignment;
import dk.aau.sw402F15.TypeChecker.Exceptions.IllegalComparison;
import dk.aau.sw402F15.TypeChecker.Exceptions.IllegalExpression;
import dk.aau.sw402F15.TypeChecker.Symboltable.Scope;
import dk.aau.sw402F15.TypeChecker.Symboltable.SymbolType;
import dk.aau.sw402F15.parser.analysis.DepthFirstAdapter;
import dk.aau.sw402F15.parser.node.*;

import java.util.Stack;

/**
 * Created by Mikkel on 08-04-2015.
 */
public class TypeChecker extends DepthFirstAdapter {
    private Stack<SymbolType> stack = new Stack<SymbolType>();
    private final Scope rootScope;
    private Scope currentScope;

    public TypeChecker(Scope rootScope) {
        this.rootScope = rootScope;
        this.currentScope = rootScope;
    }

    @Override
    public void inAScopeStatement(AScopeStatement node) {
        super.inAScopeStatement(node);
        currentScope = currentScope.getSubScopeByNodeOrThrow(node);
    }

    @Override
    public void outAFunctionCallExpr(AFunctionCallExpr node) {
        stack.push(currentScope.getSymbol(node.getName().getText()).getType());
    }

    @Override
    public void outAScopeStatement(AScopeStatement node) {
        super.outAScopeStatement(node);
        currentScope = currentScope.getParentScope();
    }

    @Override
    public void outAAssignmentDeclaration(AAssignmentDeclaration node) {
        super.outAAssignmentDeclaration(node);

        SymbolType arg1;
        SymbolType arg2;

        while ((arg1 = stack.pop()) == SymbolType.Function)
        {
            // pop again
        }

        while ((arg2 = stack.pop()) == SymbolType.Function)
        {
            // pop again
        }

        if (arg1 != arg2) {
            throw new IllegalAssignment();
        }
    }

    @Override
    public void outAAssignmentExpr(AAssignmentExpr node) {
        super.outAAssignmentExpr(node);

        SymbolType arg1;
        SymbolType arg2;

        while ((arg1 = stack.pop()) == SymbolType.Function)
        {
            // pop again
        }

        while ((arg2 = stack.pop()) == SymbolType.Function)
        {
            // pop again
        }


        if (arg1 != arg2) {
            throw new IllegalAssignment();
        }
    }

    @Override
    public void caseTIdentifier(TIdentifier node) {
        stack.push(currentScope.getSymbol(node.getText()).getType());
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