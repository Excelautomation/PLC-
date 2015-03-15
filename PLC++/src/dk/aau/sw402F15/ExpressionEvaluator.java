package dk.aau.sw402F15;

import dk.aau.sw402F15.parser.analysis.DepthFirstAdapter;
import dk.aau.sw402F15.parser.node.*;

import java.util.Stack;

public class ExpressionEvaluator extends DepthFirstAdapter {
    Stack<Boolean> booleanStack = new Stack<Boolean>();
    Stack<Integer> numberStack = new Stack<Integer>();

    @Override
    public void outALogicExprExpr(ALogicExprExpr node) {
        super.outALogicExprExpr(node);

        System.out.println("Output: " + booleanStack.pop());
        System.out.println("");
    }

    @Override
    public void outAValueExprExpr(AValueExprExpr node) {
        super.outAValueExprExpr(node);

        System.out.println("Output: " + numberStack.pop());
        System.out.println("");
    }

    @Override
    public void outAAssignmentStatement(AAssignmentStatement node) {
        super.outAAssignmentStatement(node);

        System.out.println("Output: " + booleanStack.pop());
        System.out.println("");
    }

    @Override
    public void outATrueLogicValue(ATrueLogicValue node) {
        super.outATrueLogicValue(node);

        booleanStack.push(true);
        System.out.println("True");
    }

    @Override
    public void outAFalseLogicValue(AFalseLogicValue node) {
        super.outAFalseLogicValue(node);

        booleanStack.push(false);
        System.out.println("False");
    }

    @Override
    public void outACompareAndLogicExpr(ACompareAndLogicExpr node) {
        super.outACompareAndLogicExpr(node);

        System.out.println("AND");

        // Remember short circuit
        boolean arg1 = booleanStack.pop(), arg2 = booleanStack.pop();
        booleanStack.push(arg1 && arg2);
    }

    @Override
    public void outACompareOrLogicExpr(ACompareOrLogicExpr node) {
        super.outACompareOrLogicExpr(node);

        System.out.println("OR");

        // Remember short circuit
        boolean arg1 = booleanStack.pop(), arg2 = booleanStack.pop();
        booleanStack.push(arg1 || arg2);
    }

    @Override
    public void outACompareEqualLogicTerm(ACompareEqualLogicTerm node) {
        super.outACompareEqualLogicTerm(node);

        System.out.println("Equal");

        boolean arg1 = booleanStack.pop(), arg2 = booleanStack.pop();
        booleanStack.push(arg1 == arg2);
    }

    @Override
    public void outACompareNotEqualLogicTerm(ACompareNotEqualLogicTerm node) {
        super.outACompareNotEqualLogicTerm(node);

        System.out.println("Not equal");

        boolean arg1 = booleanStack.pop(), arg2 = booleanStack.pop();
        booleanStack.push(arg1 != arg2);
    }

    @Override
    public void outAIntegerNumber(AIntegerNumber node) {
        super.outAIntegerNumber(node);

        numberStack.push(Integer.parseInt(node.getInteger().getText()));
    }

    @Override
    public void outANegnumberValue(ANegnumberValue node) {
        super.outANegnumberValue(node);

        numberStack.push(numberStack.pop() * -1);
    }


}
