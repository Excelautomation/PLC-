package dk.aau.sw402F15;

import dk.aau.sw402F15.TypeChecker.Symboltable.SymbolType;
import dk.aau.sw402F15.parser.analysis.DepthFirstAdapter;
import dk.aau.sw402F15.parser.node.*;

import java.util.Stack;

public class ExpressionEvaluator extends DepthFirstAdapter {

    Stack<Object> stack = new Stack<Object>();

    @Override
    public void outAAssignmentExpr(AAssignmentExpr node) {
        super.outAAssignmentExpr(node);

        System.out.println("Resultat: " + stack.pop());
        System.out.println();
    }

    @Override
    public void outAAssignmentDeclaration(AAssignmentDeclaration node) {
        super.outAAssignmentDeclaration(node);

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
    public void outACompareEqualExpr(ACompareEqualExpr node) {
        super.outACompareEqualExpr(node);

        Boolean arg2 = (Boolean) stack.pop(), arg1 = (Boolean) stack.pop();

        System.out.println("Equal");

        stack.push(arg1 == arg2);
    }

    @Override
    public void outACompareNotEqualExpr(ACompareNotEqualExpr node) {
        super.outACompareNotEqualExpr(node);

        Boolean arg2 = (Boolean) stack.pop(), arg1 = (Boolean) stack.pop();

        System.out.println("Not equal");

        stack.push(arg1 != arg2);
    }

    @Override
    public void outACompareGreaterExpr(ACompareGreaterExpr node) {
        super.outACompareGreaterExpr(node);

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
    public void outACompareGreaterOrEqualExpr(ACompareGreaterOrEqualExpr node) {
        super.outACompareGreaterOrEqualExpr(node);

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
    public void outACompareLessExpr(ACompareLessExpr node) {
        super.outACompareLessExpr(node);

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
    public void outACompareLessOrEqualExpr(ACompareLessOrEqualExpr node) {
        super.outACompareLessOrEqualExpr(node);

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
    public void outAAddExpr(AAddExpr node) {
        super.outAAddExpr(node);

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
    public void outASubExpr(ASubExpr node) {
        super.outASubExpr(node);

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
    public void outAMultiExpr(AMultiExpr node) {
        super.outAMultiExpr(node);

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
    public void outADivExpr(ADivExpr node) {
        super.outADivExpr(node);

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
    public void outAModExpr(AModExpr node) {
        super.outAModExpr(node);

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
    public void outATrueExpr(ATrueExpr node) {
        super.outATrueExpr(node);

        System.out.println("True");
        stack.push(true);
    }

    @Override
    public void outAFalseExpr(AFalseExpr node) {
        super.outAFalseExpr(node);

        System.out.println("False");
        stack.push(false);
    }

    @Override
    public void outADecimalExpr(ADecimalExpr node) {
        super.outADecimalExpr(node);

        System.out.println(node.getDecimalLiteral().getText());

        stack.push(Float.parseFloat(node.getDecimalLiteral().getText()));
    }

    @Override
    public void outAIntegerExpr(AIntegerExpr node) {
        super.outAIntegerExpr(node);

        System.out.println(node.getIntegerLiteral().getText());

        stack.push(Integer.parseInt(node.getIntegerLiteral().getText()));
    }



    @Override
    public void outANegationExpr(ANegationExpr node) {
        super.outANegationExpr(node);

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
