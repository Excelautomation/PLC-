package dk.aau.sw402F15.TypeChecker;

import dk.aau.sw402F15.Symboltable.Scope;
import dk.aau.sw402F15.Symboltable.Symbol;
import dk.aau.sw402F15.Symboltable.SymbolVariable;
import dk.aau.sw402F15.Symboltable.Type.SymbolType;
import dk.aau.sw402F15.TypeChecker.Exceptions.IllegalAssignmentException;
import dk.aau.sw402F15.TypeChecker.Exceptions.RedefinitionOfReadOnlyException;
import dk.aau.sw402F15.parser.analysis.DepthFirstAdapter;
import dk.aau.sw402F15.parser.node.AAssignmentExpr;
import dk.aau.sw402F15.parser.node.AIdentifierExpr;
import dk.aau.sw402F15.parser.node.AMemberExpr;

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

        if (expressionTypeEvaluator.getResult().getType() != symbolType.getType()) {
            throw new IllegalAssignmentException();
        }
    }

    @Override
    public void caseAMemberExpr(AMemberExpr node) {
        MemberExpressionEvaluator memberExpressionEvaluator = new MemberExpressionEvaluator(scope);
        node.apply(memberExpressionEvaluator);

        this.symbolType = memberExpressionEvaluator.getSymbol().getType();
    }

    @Override
    public void caseAIdentifierExpr(AIdentifierExpr node) {
        Symbol symbol = scope.getSymbolOrThrow(node.getName().getText());

        if (symbol.getClass() == SymbolVariable.class) {
            SymbolVariable variable = (SymbolVariable) symbol;

            if (variable.isConst())
                throw new RedefinitionOfReadOnlyException();


            this.symbolType = variable.getType();
        }
    }
}
