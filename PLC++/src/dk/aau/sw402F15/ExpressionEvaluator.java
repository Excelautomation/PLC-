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
        booleanStack.push(arg2 && arg1);
    }

    @Override
    public void outACompareOrLogicExpr(ACompareOrLogicExpr node) {
        super.outACompareOrLogicExpr(node);

        System.out.println("OR");

        // Remember short circuit
        boolean arg1 = booleanStack.pop(), arg2 = booleanStack.pop();
        booleanStack.push(arg2 || arg1);
    }

    @Override
    public void outACompareEqualLogicTerm(ACompareEqualLogicTerm node) {
        super.outACompareEqualLogicTerm(node);

        System.out.println("Equal");

        boolean arg1 = booleanStack.pop(), arg2 = booleanStack.pop();
        booleanStack.push(arg2 == arg1);
    }

    @Override
    public void outACompareNotEqualLogicTerm(ACompareNotEqualLogicTerm node) {
        super.outACompareNotEqualLogicTerm(node);

        System.out.println("Not equal");

        boolean arg1 = booleanStack.pop(), arg2 = booleanStack.pop();
        booleanStack.push(arg2 != arg1);
    }


    @Override
    public void caseAIntegerNumber(AIntegerNumber node) {
        super.caseAIntegerNumber(node);

        System.out.println(node.getIntegerLiteral().getText());

        numberStack.push(Integer.parseInt(node.getIntegerLiteral().getText()));
    }

    @Override
    public void outANegnumberValue(ANegnumberValue node) {
        super.outANegnumberValue(node);

        System.out.println("-");
        numberStack.push(numberStack.pop() * -1);
    }

    @Override
    public void outACompareGreaterLogicCompare(ACompareGreaterLogicCompare node) {
        super.outACompareGreaterLogicCompare(node);

        System.out.println("Greater than");

        int arg1 = numberStack.pop(), arg2 = numberStack.pop();
        booleanStack.push(arg2 > arg1);
    }

    @Override
    public void outACompareGreaterOrEqualLogicCompare(ACompareGreaterOrEqualLogicCompare node) {
        super.outACompareGreaterOrEqualLogicCompare(node);

        System.out.println("Greater than or equal");

        int arg1 = numberStack.pop(), arg2 = numberStack.pop();
        booleanStack.push(arg2 >= arg1);
    }

    @Override
    public void outACompareLessLogicCompare(ACompareLessLogicCompare node) {
        super.outACompareLessLogicCompare(node);

        System.out.println("Less than");

        int arg1 = numberStack.pop(), arg2 = numberStack.pop();
        booleanStack.push(arg2 < arg1);
    }

    @Override
    public void outACompareLessOrEqualLogicCompare(ACompareLessOrEqualLogicCompare node) {
        super.outACompareLessOrEqualLogicCompare(node);

        System.out.println("Less than or equal");

        int arg1 = numberStack.pop(), arg2 = numberStack.pop();
        booleanStack.push(arg2 <= arg1);
    }

    @Override
    public void outAAddValueExpr(AAddValueExpr node) {
        super.outAAddValueExpr(node);

        System.out.println("Add");

        int arg1 = numberStack.pop(), arg2 = numberStack.pop();
        numberStack.push(arg2 + arg1);
    }

    @Override
    public void outASubValueExpr(ASubValueExpr node) {
        super.outASubValueExpr(node);

        System.out.println("Sub");

        int arg1 = numberStack.pop(), arg2 = numberStack.pop();
        numberStack.push(arg2 - arg1);
    }

    @Override
    public void outAMultiValueFactor(AMultiValueFactor node) {
        super.outAMultiValueFactor(node);

        System.out.println("Multi");

        int arg1 = numberStack.pop(), arg2 = numberStack.pop();
        numberStack.push(arg2 * arg1);
    }

    @Override
    public void outADivValueFactor(ADivValueFactor node) {
        super.outADivValueFactor(node);

        System.out.println("Div");

        int arg1 = numberStack.pop(), arg2 = numberStack.pop();
        numberStack.push(arg2 / arg1);
    }

    @Override
    public void outAModValueFactor(AModValueFactor node) {
        super.outAModValueFactor(node);

        System.out.println("Mod");

        int arg1 = numberStack.pop(), arg2 = numberStack.pop();
        numberStack.push(arg2 % arg1);
    }
}
