package dk.aau.sw402F15;

import dk.aau.sw402F15.parser.analysis.DepthFirstAdapter;
import dk.aau.sw402F15.parser.node.*;

import java.util.Stack;

public class ExpressionEvaluator extends DepthFirstAdapter {
    Stack<Object> stack = new Stack<Object>();

    @Override
    public void outAAssignmentAssignmentStatement(AAssignmentAssignmentStatement node) {
        super.outAAssignmentAssignmentStatement(node);

        System.out.println("Resultat: " + stack.pop());
        System.out.println();
    }

    @Override
    public void outACompareAndExpr(ACompareAndExpr node) {
        super.outACompareAndExpr(node);

        Boolean arg2 = (Boolean) stack.pop(), arg1 = (Boolean) stack.pop();

        System.out.println("And");

        stack.push(arg1 && arg2);
    }

    @Override
    public void outACompareOrExpr(ACompareOrExpr node) {
        super.outACompareOrExpr(node);

        Boolean arg2 = (Boolean) stack.pop(), arg1 = (Boolean) stack.pop();

        System.out.println("Or");

        stack.push(arg1 || arg2);
    }

    @Override
    public void outACompareEqualExpr2(ACompareEqualExpr2 node) {
        super.outACompareEqualExpr2(node);

        Boolean arg2 = (Boolean) stack.pop(), arg1 = (Boolean) stack.pop();

        System.out.println("Equal");

        stack.push(arg1 == arg2);
    }

    @Override
    public void outACompareNotEqualExpr2(ACompareNotEqualExpr2 node) {
        super.outACompareNotEqualExpr2(node);

        Boolean arg2 = (Boolean) stack.pop(), arg1 = (Boolean) stack.pop();

        System.out.println("Not equal");

        stack.push(arg1 != arg2);
    }

    @Override
    public void outACompareGreaterExpr3(ACompareGreaterExpr3 node) {
        super.outACompareGreaterExpr3(node);

        Object arg2 = stack.pop(), arg1 = stack.pop();

        if (arg1 instanceof Float || arg2 instanceof Float)
        {
            System.out.println("Greater (Float)");

            stack.push((Float) arg1 > (Float) arg2);
        }
        else {
            System.out.println("Greater (Integer)");

            stack.push((Integer) arg1 > (Integer) arg2);
        }
    }

    @Override
    public void outACompareGreaterOrEqualExpr3(ACompareGreaterOrEqualExpr3 node) {
        super.outACompareGreaterOrEqualExpr3(node);

        Object arg2 = stack.pop(), arg1 = stack.pop();

        if (arg1 instanceof Float || arg2 instanceof Float)
        {
            System.out.println("Greater or equal (Float)");

            stack.push((Float) arg1 >= (Float) arg2);
        }
        else {
            System.out.println("Greater or equal (Integer)");

            stack.push((Integer) arg1 >= (Integer) arg2);
        }
    }

    @Override
    public void outACompareLessExpr3(ACompareLessExpr3 node) {
        super.outACompareLessExpr3(node);

        Object arg2 = stack.pop(), arg1 = stack.pop();

        if (arg1 instanceof Float || arg2 instanceof Float)
        {
            System.out.println("Less (Float)");

            stack.push((Float) arg1 < (Float) arg2);
        }
        else {
            System.out.println("Less (Integer)");

            stack.push((Integer) arg1 < (Integer) arg2);
        }
    }

    @Override
    public void outACompareLessOrEqualExpr3(ACompareLessOrEqualExpr3 node) {
        super.outACompareLessOrEqualExpr3(node);

        Object arg2 = stack.pop(), arg1 = stack.pop();

        if (arg1 instanceof Float || arg2 instanceof Float)
        {
            System.out.println("Less or equal (Float)");

            stack.push((Float) arg1 <= (Float) arg2);
        }
        else {
            System.out.println("Less or equal (Integer)");

            stack.push((Integer) arg1 <= (Integer) arg2);
        }
    }

    @Override
    public void outAAddExpr4(AAddExpr4 node) {
        super.outAAddExpr4(node);

        Object arg2 = stack.pop(), arg1 = stack.pop();
        if (arg1 instanceof Float || arg2 instanceof Float)
        {
            System.out.println("Add (Float)");

            stack.push((Float) arg1 + (Float) arg2);
        }
        else {
            System.out.println("Add (Integer)");

            stack.push((Integer) arg1 + (Integer) arg2);
        }
    }

    @Override
    public void outASubExpr4(ASubExpr4 node) {
        super.outASubExpr4(node);

        Object arg2 = stack.pop(), arg1 = stack.pop();
        if (arg1 instanceof Float || arg2 instanceof Float)
        {
            System.out.println("Sub (Float)");

            stack.push((Float) arg1 - (Float) arg2);
        }
        else {
            System.out.println("Sub (Integer)");

            stack.push((Integer) arg1 - (Integer) arg2);
        }
    }

    @Override
    public void outAMultiExpr5(AMultiExpr5 node) {
        super.outAMultiExpr5(node);

        Object arg2 = stack.pop(), arg1 = stack.pop();
        if (arg1 instanceof Float || arg2 instanceof Float)
        {
            System.out.println("Multi (Float)");

            stack.push((Float) arg1 * (Float) arg2);
        }
        else {
            System.out.println("Multi (Integer)");

            stack.push((Integer) arg1 * (Integer) arg2);
        }
    }

    @Override
    public void outADivExpr5(ADivExpr5 node) {
        super.outADivExpr5(node);

        Object arg2 = stack.pop(), arg1 = stack.pop();
        if (arg1 instanceof Float || arg2 instanceof Float)
        {
            System.out.println("Div (Float)");

            stack.push((Float) arg1 / (Float) arg2);
        }
        else {
            System.out.println("Div (Integer)");

            stack.push((Integer) arg1 / (Integer) arg2);
        }
    }

    @Override
    public void outAModExpr5(AModExpr5 node) {
        super.outAModExpr5(node);

        Object arg2 = stack.pop(), arg1 = stack.pop();
        if (arg1 instanceof Float || arg2 instanceof Float)
        {
            System.out.println("Mod (Float)");

            stack.push((Float) arg1 % (Float) arg2);
        }
        else {
            System.out.println("Mod (Integer)");

            stack.push((Integer) arg1 % (Integer) arg2);
        }
    }

    @Override
    public void outATrueExprValue(ATrueExprValue node) {
        super.outATrueExprValue(node);

        System.out.println("True");
        stack.push(true);
    }

    @Override
    public void outAFalseExprValue(AFalseExprValue node) {
        super.outAFalseExprValue(node);

        System.out.println("False");
        stack.push(false);
    }

    @Override
    public void outADecimalNumber(ADecimalNumber node) {
        super.outADecimalNumber(node);

        System.out.println(node.getDecimalLiteral().getText());

        stack.push(Float.parseFloat(node.getDecimalLiteral().getText()));
    }

    @Override
    public void outAIntegerNumber(AIntegerNumber node) {
        super.outAIntegerNumber(node);

        System.out.println(node.getIntegerLiteral().getText());

        stack.push(Integer.parseInt(node.getIntegerLiteral().getText()));
    }

    @Override
    public void outANegnumberValue(ANegnumberValue node) {
        super.outANegnumberValue(node);

        System.out.println("-");

        Object arg1 = stack.pop();

        if (arg1 instanceof Float)
        {
            stack.push(-1f * (Float) arg1);
        }
        else {
            stack.push(-1 * (Integer) arg1);
        }
    }
}
