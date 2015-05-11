package dk.aau.sw402F15.TypeChecker;

import dk.aau.sw402F15.Exception.TypeChecker.IncompaitbleTypesException;
import dk.aau.sw402F15.Exception.TypeChecker.RedefinitionOfConstException;
import dk.aau.sw402F15.Symboltable.Scope;
import dk.aau.sw402F15.Symboltable.Symbol;
import dk.aau.sw402F15.Symboltable.SymbolArray;
import dk.aau.sw402F15.Symboltable.SymbolVariable;
import dk.aau.sw402F15.Symboltable.Type.SymbolType;
import dk.aau.sw402F15.parser.analysis.DepthFirstAdapter;
import dk.aau.sw402F15.parser.node.*;

/**
 * Created by sahb on 27/04/15.
 */
public class AssignmentTypeChecker extends DepthFirstAdapter {
    private Scope scope;
    private SymbolType symbolType;

    public AssignmentTypeChecker(Scope scope) {
        this.scope = scope;
    }

    public SymbolType getResult() {
        return symbolType;
    }

    @Override
    public void caseAAssignmentExpr(AAssignmentExpr node) {
        node.getLeft().apply(this);

        ExpressionTypeEvaluator expressionTypeEvaluator = new ExpressionTypeEvaluator(scope);
        node.getRight().apply(expressionTypeEvaluator);

        SymbolType exprResultType = expressionTypeEvaluator.getResult();

        // Check if we must make a implicit type conversion
        if (exprResultType.equals(SymbolType.Int()) && symbolType.equals(SymbolType.Decimal())) {
            // Int is promoted to decimal
            // Add typecast
            node.getRight().replaceBy(new ATypeCastExpr(new ADoubleTypeSpecifier(), (PExpr) node.getRight().clone()));
            return;
        }

        // Check if we could match the correct type
        if (!exprResultType.equals(symbolType)) {
            throw new IncompaitbleTypesException(node, exprResultType, symbolType);
        }
    }

    @Override
    public void caseAMemberExpr(AMemberExpr node) {
        MemberExpressionEvaluator memberExpressionEvaluator = new MemberExpressionEvaluator(scope);
        node.apply(memberExpressionEvaluator);

        this.symbolType = memberExpressionEvaluator.getSymbol();
    }

    @Override
    public void caseAIdentifierExpr(AIdentifierExpr node) {
        Symbol symbol = scope.getSymbolOrThrow(node.getName().getText(), node);
        SymbolVariable variable = (SymbolVariable) symbol;

        if (variable.isConst())
            throw new RedefinitionOfConstException(node);


        this.symbolType = variable.getType();
    }

    @Override
    public void caseAArrayExpr(AArrayExpr node) {
        Symbol symbol = scope.getSymbolOrThrow(node.getName().getText(), node);
        SymbolArray array = (SymbolArray) symbol;

        this.symbolType = array.getContainedType();
    }
}
