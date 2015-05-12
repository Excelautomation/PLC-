package dk.aau.sw402F15.TypeChecker;

import dk.aau.sw402F15.Exception.TypeChecker.*;
import dk.aau.sw402F15.Helper;
import dk.aau.sw402F15.Symboltable.Scope;
import dk.aau.sw402F15.Symboltable.Symbol;
import dk.aau.sw402F15.Symboltable.SymbolArray;
import dk.aau.sw402F15.Symboltable.SymbolFunction;
import dk.aau.sw402F15.Symboltable.Type.SymbolType;
import dk.aau.sw402F15.parser.analysis.DepthFirstAdapter;
import dk.aau.sw402F15.parser.node.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by sahb on 24/04/15.
 */
public class ExpressionTypeEvaluator extends DepthFirstAdapter {
    private Stack<SymbolType> stack;
    private Scope scope;

    public ExpressionTypeEvaluator(Scope scope) {
        this.stack = new Stack<SymbolType>();
        this.scope = scope;
    }

    public SymbolType getResult() {
        // Check if we only have 1 element on stack
        if (stack.size() != 1) {
            throw new NotImplementedException();
        }

        return stack.pop();
    }

    // Assignment
    @Override
    public void caseAAssignmentExpr(AAssignmentExpr node) {
        AssignmentTypeChecker assignmentChecker = new AssignmentTypeChecker(scope);
        node.apply(assignmentChecker);

        stack.push(assignmentChecker.getResult());
    }

    // Member expression
    @Override
    public void caseAMemberExpr(AMemberExpr node) {
        MemberExpressionEvaluator memberExpressionEvaluator = new MemberExpressionEvaluator(scope);
        node.apply(memberExpressionEvaluator);

        stack.push(memberExpressionEvaluator.getSymbol());
    }

    // Function call
    @Override
    public void caseAFunctionCallExpr(AFunctionCallExpr node) {
        Symbol symbol = scope.getSymbolOrThrow(node.getName().getText(), node);

        // Check if symbol is a function - if not throw a exception
        if (symbol.getClass() != SymbolFunction.class) {
            throw new RuntimeException();
        }

        // Cast to symbolfunction
        SymbolFunction func = (SymbolFunction) symbol;

        // Check arguments
        {
            ExpressionTypeEvaluator expressionEvaluator = new ExpressionTypeEvaluator(scope);
            List<PExpr> copy = new ArrayList<PExpr>(node.getArgs());

            // Check number of parameters
            if (copy.size() != func.getFormalParameters().size())
                throw new WrongNumberOfParameters(node, func.getFormalParameters().size(), copy.size());

            // Check each expression
            for (int i = 0; i < copy.size(); i++) {
                PExpr e = copy.get(i);

                // Get expression type
                e.apply(expressionEvaluator);

                SymbolType type = expressionEvaluator.getResult();
                if (!type.equals(func.getFormalParameters().get(i)))
                    throw new WrongParameterTypeException(node, func.getFormalParameters().get(i), type);
            }
        }

        // Push return type to stack
        stack.push(func.getReturnType());
    }

    // Identifier (variables)
    @Override
    public void outAIdentifierExpr(AIdentifierExpr node) {
        super.outAIdentifierExpr(node);

        stack.push(scope.getSymbolOrThrow(node.getName().getText(), node).getType());
    }

    // Constants
    @Override
    public void outAIntegerExpr(AIntegerExpr node) {
        super.outAIntegerExpr(node);
        stack.push(SymbolType.Int());
    }

    @Override
    public void outADecimalExpr(ADecimalExpr node) {
        super.outADecimalExpr(node);
        stack.push(SymbolType.Decimal());
    }

    @Override
    public void outATrueExpr(ATrueExpr node) {
        super.outATrueExpr(node);
        stack.push(SymbolType.Boolean());
    }

    @Override
    public void outAFalseExpr(AFalseExpr node) {
        super.outAFalseExpr(node);
        stack.push(SymbolType.Boolean());
    }

    @Override
    public void outAPortInputExpr(APortInputExpr node) {
        super.outAPortInputExpr(node);
        if (stack.pop().equals(SymbolType.Type.Decimal)) {
            stack.push(SymbolType.PortInput());
        }
        else {
            throw new InvalidPortException(node);
        }
    }

    @Override
    public void outAPortOutputExpr(APortOutputExpr node) {
        super.outAPortOutputExpr(node);
        if (stack.pop().equals(SymbolType.Type.Decimal)) {
            stack.push(SymbolType.PortOuput());
        }
        else {
            throw new InvalidPortException(node);
        }
    }

    @Override
    public void outAPortMemoryExpr(APortMemoryExpr node) {
        super.outAPortMemoryExpr(node);
        if (stack.pop().equals(SymbolType.Type.Int)) {
            stack.push(SymbolType.PortMemory());
        }
        else {
            throw new InvalidPortException(node);
        }
    }

    @Override
    public void outAPortAnalogInputExpr(APortAnalogInputExpr node) {
        super.outAPortAnalogInputExpr(node);
        throw new NotImplementedException();
    }

    @Override
    public void outAPortAnalogOutputExpr(APortAnalogOutputExpr node) {
        super.outAPortAnalogOutputExpr(node);
        throw new NotImplementedException();
    }

    // Casts
    @Override
    public void outATypeCastExpr(ATypeCastExpr node) {
        super.outATypeCastExpr(node);

        // Cast no typechecker test
        stack.pop();
        stack.push(Helper.getSymbolTypeFromTypeSpecifier(node.getTargetType()));
    }

    // Increment and decrement
    @Override
    public void inAIncrementExpr(AIncrementExpr node) {
        super.inAIncrementExpr(node);

        // Push identifier on stack
        stack.push(scope.getSymbolOrThrow(node.getName().getText(), node).getType());
    }

    @Override
    public void outAIncrementExpr(AIncrementExpr node) {
        super.outAIncrementExpr(node);
        checkUnary(node);
    }

    @Override
    public void inADecrementExpr(ADecrementExpr node) {
        super.inADecrementExpr(node);

        // Push identifier on stack
        stack.push(scope.getSymbolOrThrow(node.getName().getText(), node).getType());
    }

    @Override
    public void outADecrementExpr(ADecrementExpr node) {
        super.outADecrementExpr(node);
        checkUnary(node);
    }

    // Unary plus and minus
    @Override
    public void outAUnaryPlusExpr(AUnaryPlusExpr node) {
        super.outAUnaryPlusExpr(node);
        checkUnary(node);
    }

    @Override
    public void outAUnaryMinusExpr(AUnaryMinusExpr node) {
        super.outAUnaryMinusExpr(node);
        checkUnary(node);
    }

    // Negation
    @Override
    public void outANegationExpr(ANegationExpr node) {
        super.outANegationExpr(node);
        checkUnaryBool(node);
    }

    // Logic comparison
    @Override
    public void outACompareAndExpr(ACompareAndExpr node) {
        super.outACompareAndExpr(node);
        checkLocicComparison(node, node.getLeft(), node.getRight());
    }

    @Override
    public void outACompareOrExpr(ACompareOrExpr node) {
        super.outACompareOrExpr(node);
        checkLocicComparison(node, node.getLeft(), node.getRight());
    }

    // Comparison
    @Override
    public void outACompareGreaterExpr(ACompareGreaterExpr node) {
        super.outACompareGreaterExpr(node);
        checkComparison(node, node.getLeft(), node.getRight());
    }

    @Override
    public void outACompareLessExpr(ACompareLessExpr node) {
        super.outACompareLessExpr(node);
        checkComparison(node, node.getLeft(), node.getRight());
    }

    @Override
    public void outACompareLessOrEqualExpr(ACompareLessOrEqualExpr node) {
        super.outACompareLessOrEqualExpr(node);
        checkComparison(node, node.getLeft(), node.getRight());
    }

    @Override
    public void outACompareGreaterOrEqualExpr(ACompareGreaterOrEqualExpr node) {
        super.outACompareGreaterOrEqualExpr(node);
        checkComparison(node, node.getLeft(), node.getRight());
    }

    @Override
    public void outACompareEqualExpr(ACompareEqualExpr node) {
        super.outACompareEqualExpr(node);
        checkComparisonEquality(node, node.getLeft(), node.getRight());
    }

    @Override
    public void outACompareNotEqualExpr(ACompareNotEqualExpr node) {
        super.outACompareNotEqualExpr(node);
        checkComparisonEquality(node, node.getLeft(), node.getRight());
    }

    // Math operations
    @Override
    public void outAAddExpr(AAddExpr node) {
        super.outAAddExpr(node);
        checkExpression(node, node.getLeft(), node.getRight());
    }

    @Override
    public void outASubExpr(ASubExpr node) {
        super.outASubExpr(node);
        checkExpression(node, node.getLeft(), node.getRight());
    }

    @Override
    public void outAMultiExpr(AMultiExpr node) {
        super.outAMultiExpr(node);
        checkExpression(node, node.getLeft(), node.getRight());
    }

    @Override
    public void outADivExpr(ADivExpr node) {
        super.outADivExpr(node);
        checkExpression(node, node.getLeft(), node.getRight());

        // Checking that we don't divide by zero
        if (node.getRight().getClass() == AIntegerExpr.class) {
            if (Integer.parseInt(((AIntegerExpr) node.getRight()).getIntegerLiteral().getText()) == 0)
                throw new DivisionByZeroException(node);
        } else if (node.getRight().getClass() == ADecimalExpr.class) {
            if (Float.parseFloat(((ADecimalExpr) node.getRight()).getDecimalLiteral().getText()) == 0.0)
                throw new DivisionByZeroException(node);
        }
    }

    @Override
    public void outAModExpr(AModExpr node) {
        super.outAModExpr(node);
        checkExpression(node, node.getLeft(), node.getRight());
    }

    // Ternary expr
    @Override
    public void outATernaryExpr(ATernaryExpr node) {
        super.outATernaryExpr(node);
        // expr = expr ? expr
        // All 3 types needs to be equal
        SymbolType arg3 = stack.pop(), arg2 = stack.pop(), arg1 = stack.pop();
        if (!arg1.equals(arg2) || !arg2.equals(arg3)) {
            throw new IncompaitbleTypesException(node, arg1, arg2, arg3);
        }
    }

    // Array expr
    @Override
    public void outAArrayExpr(AArrayExpr node) {
        super.outAArrayExpr(node);

        // Get type of index (needs to be an int, since index cannot be a floating point number)
        SymbolType arg1 = stack.pop();
        if (!arg1.equals(SymbolType.Int())) {
            throw new ArrayIndexIsIntException(node);
        }

        // Check identifier it needs to be an array
        Symbol symbol = scope.getSymbolOrThrow(node.getName().getText(), node);
        if (!symbol.getType().equals(SymbolType.Array())) {
            throw new ArrayIndexOfNonArrayException(node);
        }

        // Push correct type to stack
        SymbolArray array = (SymbolArray) symbol;
        stack.push(array.getContainedType());
    }

    // Helper methods for compare and math operations
    // == !=
    private void checkComparisonEquality(Node node, PExpr left, PExpr right) {
        SymbolType arg2 = stack.pop(), arg1 = stack.pop();

        if (arg1.equals(SymbolType.Type.PortInput) && arg2.equals(SymbolType.Type.PortInput)) {
            // Cast left
            left.replaceBy(new ATypeCastExpr(new ABoolTypeSpecifier(), (PExpr) left.clone()));
            // Cast right
            right.replaceBy(new ATypeCastExpr(new ABoolTypeSpecifier(), (PExpr) right.clone()));

            stack.push(SymbolType.Boolean());
        } else if (arg1.equals(arg2)) {
            stack.push(SymbolType.Boolean());
        } else if (arg1.equals(SymbolType.Type.PortInput) && arg2.equals(SymbolType.Type.Boolean)) {
            // Cast left
            left.replaceBy(new ATypeCastExpr(new ABoolTypeSpecifier(), (PExpr) left.clone()));
            stack.push(SymbolType.Boolean());
        } else if (arg2.equals(SymbolType.Type.PortInput) && arg1.equals(SymbolType.Type.Boolean)) {
            // Cast right
            right.replaceBy(new ATypeCastExpr(new ABoolTypeSpecifier(), (PExpr) right.clone()));
            stack.push(SymbolType.Boolean());
        } else {
            throw new IncompaitbleTypesException(node, arg1, arg2);
        }
    }

    // > >= < <=
    private void checkComparison(Node node, PExpr left, PExpr right) {
        SymbolType arg2 = stack.pop(), arg1 = stack.pop();

        if (arg1.equals(SymbolType.Type.Int) && arg2.equals(SymbolType.Type.Int)) {
            stack.push(SymbolType.Boolean());
        } else if (arg1.equals(SymbolType.Type.Decimal) && arg2.equals(SymbolType.Type.Decimal)) {
            stack.push(SymbolType.Boolean());
        } else if (arg1.equals(SymbolType.Type.Decimal) && arg2.equals(SymbolType.Type.Int)) {
            // Promote right
            right.replaceBy(new ATypeCastExpr(new ADoubleTypeSpecifier(), (PExpr) right.clone()));
            stack.push(SymbolType.Boolean());
        } else if (arg1.equals(SymbolType.Type.Int) && arg2.equals(SymbolType.Type.Decimal)) {
            // Promote left
            left.replaceBy(new ATypeCastExpr(new ADoubleTypeSpecifier(), (PExpr) left.clone()));
            stack.push(SymbolType.Boolean());
        } else {
            throw new IncompaitbleTypesException(node, arg1, arg2);
        }
    }

    // && ||
    private void checkLocicComparison(Node node, PExpr left, PExpr right) {
        SymbolType arg2 = stack.pop(), arg1 = stack.pop();

        if ((arg1.equals(SymbolType.Type.Boolean) && arg2.equals(SymbolType.Type.Boolean))) {
            stack.push(SymbolType.Boolean());
        } else if (arg1.equals(SymbolType.Type.PortInput) && arg2.equals(SymbolType.Type.Boolean)) {
            // Cast left
            left.replaceBy(new ATypeCastExpr(new ABoolTypeSpecifier(), (PExpr) left.clone()));
            stack.push(SymbolType.Boolean());
        } else if (arg2.equals(SymbolType.Type.PortInput) && arg1.equals(SymbolType.Type.Boolean)) {
            // Cast right
            right.replaceBy(new ATypeCastExpr(new ABoolTypeSpecifier(), (PExpr) right.clone()));
            stack.push(SymbolType.Boolean());
        } else if (arg1.equals(SymbolType.Type.PortInput) && arg2.equals(SymbolType.Type.PortInput)) {
            // Cast left
            left.replaceBy(new ATypeCastExpr(new ABoolTypeSpecifier(), (PExpr) left.clone()));
            // Cast right
            right.replaceBy(new ATypeCastExpr(new ABoolTypeSpecifier(), (PExpr) right.clone()));
            stack.push(SymbolType.Boolean());
        } else {
            throw new IncompaitbleTypesException(node, arg1, arg2);
        }
    }

    // + - * / %
    private void checkExpression(Node node, PExpr left, PExpr right) {
        SymbolType arg2 = stack.pop(), arg1 = stack.pop();

        if (arg1.equals(SymbolType.Type.Int) && arg2.equals(SymbolType.Type.Int)) {
            stack.push(SymbolType.Int());
        } else if (arg1.equals(SymbolType.Type.Decimal) && arg2.equals(SymbolType.Type.Decimal)) {
            stack.push(SymbolType.Decimal());
        } else if ((arg1.equals(SymbolType.Type.Decimal) && arg2.equals(SymbolType.Type.Int))) {
            // Promote right
            right.replaceBy(new ATypeCastExpr(new ADoubleTypeSpecifier(), (PExpr) right.clone()));
            stack.push(SymbolType.Decimal());
        } else if ((arg1.equals(SymbolType.Type.Int) && arg2.equals(SymbolType.Type.Decimal))) {
            // Promote left
            left.replaceBy(new ATypeCastExpr(new ADoubleTypeSpecifier(), (PExpr) left.clone()));
            stack.push(SymbolType.Decimal());
        } else {
            throw new IncompaitbleTypesException(node, arg1, arg2);
        }
    }

    private void checkUnary(Node node) {
        // Don't pop - we don't change type
        SymbolType type = stack.peek();

        if (!type.equals(SymbolType.Type.Int) && !type.equals(SymbolType.Type.Decimal)) {
            throw new ExpectingIntOrDecimalException(node, type);
        }
    }

    private void checkUnaryBool(Node node) {
        // Don't pop - we don't change type
        SymbolType type = stack.peek();

        if (!type.equals(SymbolType.Type.Boolean)) {
            throw new ExpectingBoolException(node, type);
        }
    }
}
