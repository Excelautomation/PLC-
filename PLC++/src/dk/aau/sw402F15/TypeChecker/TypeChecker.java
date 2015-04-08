package dk.aau.sw402F15.TypeChecker;

import dk.aau.sw402F15.TypeChecker.Exceptions.IllegalAssignment;
import dk.aau.sw402F15.TypeChecker.Exceptions.IllegalComparison;
import dk.aau.sw402F15.TypeChecker.Exceptions.IllegalExpression;
import dk.aau.sw402F15.TypeChecker.Symboltable.SymbolType;
import dk.aau.sw402F15.parser.analysis.DepthFirstAdapter;
import dk.aau.sw402F15.parser.node.*;

import java.util.Stack;

/**
 * Created by Mikkel on 08-04-2015.
 */
public class TypeChecker extends DepthFirstAdapter {
    Stack<SymbolType> stack = new Stack<SymbolType>();

    @Override
    public void outADeclarationAssignmentDeclarationStatement(ADeclarationAssignmentDeclarationStatement node) {
        super.outADeclarationAssignmentDeclarationStatement(node);

        if (stack.pop() != stack.pop()) {
            throw new IllegalAssignment(); // Needs a better exception
        }
    }

    @Override
    public void outAIntegerValue(AIntegerValue node) {
        super.outAIntegerValue(node);
        stack.push(SymbolType.Int);
    }

    @Override
    public void outADecimalValue(ADecimalValue node) {
        super.outADecimalValue(node);
        stack.push(SymbolType.Decimal);
    }

    @Override
    public void outATrueValue(ATrueValue node) {
        super.outATrueValue(node);
        stack.push(SymbolType.Boolean);
    }

    @Override
    public void outAFalseValue(AFalseValue node) {
        super.outAFalseValue(node);
        stack.push(SymbolType.Boolean);
    }

    @Override
    public void outAIntType(AIntType node) {
        super.outAIntType(node);
        stack.push(SymbolType.Int);
    }

    @Override
    public void outALongType(ALongType node) {
        super.outALongType(node);
        stack.push(SymbolType.Int);
    }

    @Override
    public void outADoubleType(ADoubleType node) {
        super.outADoubleType(node);
        stack.push(SymbolType.Decimal);
    }

    @Override
    public void outAFloatType(AFloatType node) {
        super.outAFloatType(node);
        stack.push(SymbolType.Decimal);
    }

    @Override
    public void outACharType(ACharType node) {
        super.outACharType(node);
        stack.push(SymbolType.Char);
    }

    @Override
    public void outABoolType(ABoolType node) {
        super.outABoolType(node);
        stack.push(SymbolType.Boolean);


    }

    @Override
    public void outACompareGreaterExpr3(ACompareGreaterExpr3 node) {
        super.outACompareGreaterExpr3(node);
        checkComparison();
    }

    @Override
    public void outACompareLessExpr3(ACompareLessExpr3 node) {
        super.outACompareLessExpr3(node);
        checkComparison();
    }

    @Override
    public void outACompareLessOrEqualExpr3(ACompareLessOrEqualExpr3 node) {
        super.outACompareLessOrEqualExpr3(node);
        checkComparison();
    }

    @Override
    public void outACompareGreaterOrEqualExpr3(ACompareGreaterOrEqualExpr3 node) {
        super.outACompareGreaterOrEqualExpr3(node);
        checkComparison();
    }

    @Override
    public void outACompareEqualExpr2(ACompareEqualExpr2 node) {
        super.outACompareEqualExpr2(node);
        checkComparison();
    }

    @Override
    public void outACompareNotEqualExpr2(ACompareNotEqualExpr2 node) {
        super.outACompareNotEqualExpr2(node);
        checkComparison();
    }

    @Override
    public void outAAddExpr4(AAddExpr4 node) {
        super.outAAddExpr4(node);
        checkExpression();
    }

    @Override
    public void outASubExpr4(ASubExpr4 node) {
        super.outASubExpr4(node);
        checkExpression();
    }

    @Override
    public void outAMultiExpr5(AMultiExpr5 node) {
        super.outAMultiExpr5(node);
        checkExpression();
    }

    @Override
    public void outADivExpr5(ADivExpr5 node) {
        super.outADivExpr5(node);
        checkExpression();
    }

    @Override
    public void outAModExpr5(AModExpr5 node) {
        super.outAModExpr5(node);
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

        if (arg1 == SymbolType.Int || arg2 == SymbolType.Int){
            stack.push(SymbolType.Int);
        }
        else if (arg1 == SymbolType.Decimal || arg2 == SymbolType.Decimal){
            stack.push(SymbolType.Decimal);
        }
        else{
            throw new IllegalExpression();
        }
    }
}