package dk.aau.sw402F15.TypeChecker;

import dk.aau.sw402F15.Helper;
import dk.aau.sw402F15.Symboltable.*;
import dk.aau.sw402F15.Symboltable.Type.SymbolType;
import dk.aau.sw402F15.TypeChecker.Exceptions.*;
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

    /*
    Not called

    @Override
    public void outAAssignmentExpr(AAssignmentExpr node) {
        super.outAAssignmentExpr(node);

        SymbolType arg1 = stack.pop();
        SymbolType arg2 = stack.peek(); // Assignment is an expression (needs to have a returntype)

        if (arg1.getType() != arg2.getType()) {
            throw new IllegalAssignmentException();
        }
    }*/

    // Member expression
    @Override
    public void caseAMemberExpr(AMemberExpr node) {
        MemberExpressionEvaluator memberExpressionEvaluator = new MemberExpressionEvaluator(scope);
        node.apply(memberExpressionEvaluator);

        stack.push(memberExpressionEvaluator.getSymbol().getType());
    }

    // Function call
    @Override
    public void caseAFunctionCallExpr(AFunctionCallExpr node) {
        Symbol symbol = scope.getSymbol(node.getName().getText());

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
                throw new WrongParameterException();

            // Check each expression
            for (int i = 0; i < copy.size(); i++) {
                PExpr e = copy.get(i);

                // Get expression type
                e.apply(expressionEvaluator);

                SymbolType type = expressionEvaluator.getResult();
                if (type.getType() != func.getFormalParameters().get(i).getType())
                    throw new WrongParameterException();
            }
        }

        // Push return type to stack
        stack.push(func.getReturnType());
    }

    // Identifier (variables)
    @Override
    public void outAIdentifierExpr(AIdentifierExpr node) {
        super.outAIdentifierExpr(node);

        stack.push(scope.getSymbolOrThrow(node.getName().getText()).getType());
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
    public void outAIncrementExpr(AIncrementExpr node) {
        super.outAIncrementExpr(node);
        checkUnary();
    }

    @Override
    public void outADecrementExpr(ADecrementExpr node) {
        super.outADecrementExpr(node);
        checkUnary();
    }

    // Unary plus and minus
    @Override
    public void outAUnaryPlusExpr(AUnaryPlusExpr node) {
        super.outAUnaryPlusExpr(node);
        checkUnary();
    }

    @Override
    public void outAUnaryMinusExpr(AUnaryMinusExpr node) {
        super.outAUnaryMinusExpr(node);
        checkUnary();
    }

    // Negation
    @Override
    public void outANegationExpr(ANegationExpr node) {
        super.outANegationExpr(node);
        checkUnaryBool();
    }

    // Logic comparison
    @Override
    public void outACompareAndExpr(ACompareAndExpr node) {
        super.outACompareAndExpr(node);
        checkLocicComparison();
    }

    @Override
    public void outACompareOrExpr(ACompareOrExpr node) {
        super.outACompareOrExpr(node);
        checkLocicComparison();
    }

    // Comparison
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

    // Math operations
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

        // Checking that we don't divide by zero
        if (node.getRight().getClass() == AIntegerExpr.class)
        {
            if (Integer.parseInt(((AIntegerExpr) node.getRight()).getIntegerLiteral().getText()) == 0)
                throw new DivisionByZeroException();
        }
        else if (node.getRight().getClass() == ADecimalExpr.class)
        {
            if (Float.parseFloat(((ADecimalExpr) node.getRight()).getDecimalLiteral().getText()) == 0.0)
                throw new DivisionByZeroException();
        }
    }

    @Override
    public void outAModExpr(AModExpr node) {
        super.outAModExpr(node);
        checkExpression();
    }

    // Ternary expr
    @Override
    public void outATernaryExpr(ATernaryExpr node) {
        super.outATernaryExpr(node);
        // expr = expr ? expr
        // All 3 types needs to be equal
        SymbolType.Type arg3 = stack.pop().getType(), arg2 = stack.pop().getType(), arg1 = stack.pop().getType();
        if (arg1 != arg2 || arg2 != arg3) {
            throw new IllegalExpressionException();
        }
    }

    // Helper methods for compare and math operations
    private void checkComparison() {
        SymbolType arg2 = stack.pop(), arg1 = stack.pop();

        if ((arg1.getType() == SymbolType.Type.Int && arg2.getType() == SymbolType.Type.Int)
                || (arg1.getType() == SymbolType.Type.Decimal && arg2.getType() == SymbolType.Type.Decimal)
                || (arg1.getType() == SymbolType.Type.Decimal && arg2.getType() == SymbolType.Type.Int)
                || (arg1.getType() == SymbolType.Type.Int && arg2.getType() == SymbolType.Type.Decimal)) {
            stack.push(SymbolType.Boolean());
        }
        else {
            throw new IllegalComparisonException();
        }
    }

    private void checkLocicComparison() {
        SymbolType arg2 = stack.pop(), arg1 = stack.pop();

        if ((arg1.getType() == SymbolType.Type.Boolean && arg2.getType() == SymbolType.Type.Boolean)) {
            stack.push(SymbolType.Boolean());
        }
        else {
            throw new IllegalComparisonException();
        }
    }

    private void checkExpression(){
        SymbolType arg2 = stack.pop(), arg1 = stack.pop();

        if (arg1.getType() == SymbolType.Type.Int && arg2.getType() == SymbolType.Type.Int){
            stack.push(SymbolType.Int());
        }
        else if (arg1.getType() == SymbolType.Type.Decimal && arg2.getType() == SymbolType.Type.Decimal){
            stack.push(SymbolType.Decimal());
        }
        else if ((arg1.getType() == SymbolType.Type.Decimal && arg2.getType() == SymbolType.Type.Int)
                 || (arg1.getType() == SymbolType.Type.Int && arg2.getType() == SymbolType.Type.Decimal)) {
            stack.push(SymbolType.Decimal());
        }
        else{
            throw new IllegalExpressionException();
        }
    }

    private void checkUnary() {
        // Don't pop - we don't change type
        SymbolType.Type type = stack.peek().getType();

        if (type != SymbolType.Type.Int && type != SymbolType.Type.Decimal) {
            throw new IllegalExpressionException();
        }
    }

    private void checkUnaryBool() {
        // Don't pop - we don't change type
        SymbolType.Type type = stack.peek().getType();

        if (type != SymbolType.Type.Boolean) {
            throw new IllegalExpressionException();
        }
    }
}
