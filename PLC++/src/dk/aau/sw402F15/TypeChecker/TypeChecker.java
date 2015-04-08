package dk.aau.sw402F15.TypeChecker;

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
        // bool b = true;

        if (stack.pop() != stack.pop()) {
            throw new RuntimeException();
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
    public void outACompareOrExpr(ACompareOrExpr node) {
        super.outACompareOrExpr(node);

        SymbolType arg2 = stack.pop(), arg1 = stack.pop();

        if (arg1 != SymbolType.Boolean || arg2 != SymbolType.Boolean) {
            throw new RuntimeException();
        }
    }
}