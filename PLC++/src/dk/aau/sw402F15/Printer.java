package dk.aau.sw402F15;

import dk.aau.sw402F15.parser.analysis.DepthFirstAdapter;
import dk.aau.sw402F15.parser.node.*;

import java.util.Stack;

public class Printer extends DepthFirstAdapter {
    Stack<Boolean> stack = new Stack<Boolean>();

    @Override
    public void outADeclarationStatement(ADeclarationStatement node) {
        super.outADeclarationStatement(node);

        System.out.println("Output: " + stack.pop());
        System.out.println("");
    }

    @Override
    public void outAAssignmentStatement(AAssignmentStatement node) {
        super.outAAssignmentStatement(node);

        System.out.println("Output: " + stack.pop());
        System.out.println("");
    }

    @Override
    public void outATrueLogicValue(ATrueLogicValue node) {
        super.outATrueLogicValue(node);

        stack.push(true);
        System.out.println("True");
    }

    @Override
    public void outAFalseLogicValue(AFalseLogicValue node) {
        super.outAFalseLogicValue(node);

        stack.push(false);
        System.out.println("False");
    }

    @Override
    public void outACompareAndLogicExpr(ACompareAndLogicExpr node) {
        super.outACompareAndLogicExpr(node);

        System.out.println("AND");

        // Remember short circuit
        boolean arg1 = stack.pop(), arg2 = stack.pop();
        stack.push(arg1 && arg2);
    }

    @Override
    public void outACompareOrLogicExpr(ACompareOrLogicExpr node) {
        super.outACompareOrLogicExpr(node);

        System.out.println("OR");

        // Remember short circuit
        boolean arg1 = stack.pop(), arg2 = stack.pop();
        stack.push(arg1 || arg2);
    }

    @Override
    public void outACompareEqualLogicTerm(ACompareEqualLogicTerm node) {
        super.outACompareEqualLogicTerm(node);

        System.out.println("Equal");

        boolean arg1 = stack.pop(), arg2 = stack.pop();
        stack.push(arg1 == arg2);
    }

    @Override
    public void outACompareNotEqualLogicTerm(ACompareNotEqualLogicTerm node) {
        super.outACompareNotEqualLogicTerm(node);

        System.out.println("Not equal");

        boolean arg1 = stack.pop(), arg2 = stack.pop();
        stack.push(arg1 != arg2);
    }
}
