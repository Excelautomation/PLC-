package dk.aau.sw402F15.TypeChecker;

import dk.aau.sw402F15.Helper;
import dk.aau.sw402F15.Symboltable.Scope;
import dk.aau.sw402F15.Symboltable.SymbolType;
import dk.aau.sw402F15.TypeChecker.Exceptions.IllegalAssignmentException;
import dk.aau.sw402F15.parser.analysis.DepthFirstAdapter;
import dk.aau.sw402F15.parser.node.ADeclaration;

/**
 * Created by sahb on 27/04/15.
 */
public class DeclarationTypeChecker extends DepthFirstAdapter {
    private Scope scope;

    public DeclarationTypeChecker(Scope scope) {

        this.scope = scope;
    }

    @Override
    public void caseADeclaration(ADeclaration node) {
        // Check if declaration is not assigned
        if (node.getExpr() == null)
            return;

        ExpressionTypeEvaluator expressionTypeEvaluator = new ExpressionTypeEvaluator(scope);
        node.getExpr().apply(expressionTypeEvaluator);

        // Get expr type
        SymbolType exprType = expressionTypeEvaluator.getResult();

        // Check type of declaration
        SymbolType declarationType = Helper.getSymbolTypeFromTypeSpecifier(node.getType());

        // Check if types matches
        if (exprType != declarationType) {
            throw new IllegalAssignmentException();
        }
    }
}
