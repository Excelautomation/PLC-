package dk.aau.sw402F15.TypeChecker;

import dk.aau.sw402F15.Exception.TypeChecker.IncompaitbleTypesException;
import dk.aau.sw402F15.Helper;
import dk.aau.sw402F15.Symboltable.Scope;
import dk.aau.sw402F15.Symboltable.Type.SymbolType;
import dk.aau.sw402F15.parser.analysis.DepthFirstAdapter;
import dk.aau.sw402F15.parser.node.ADeclaration;
import dk.aau.sw402F15.parser.node.ADoubleTypeSpecifier;
import dk.aau.sw402F15.parser.node.ATypeCastExpr;

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
        node.apply(expressionTypeEvaluator);

        // Get expr type
        SymbolType exprType = expressionTypeEvaluator.getResult();

        // Check type of declaration
        SymbolType declarationType = Helper.getSymbolTypeFromTypeSpecifier(node.getType());

        // Check if we must make a implicit type conversion
        if (exprType.equals(SymbolType.Type.Int) && declarationType.equals(SymbolType.Type.Decimal)) {
            // Int is promoted to decimal
            // Add typecast
            node.setExpr(new ATypeCastExpr(new ADoubleTypeSpecifier(), node.getExpr()));
            return;
        }

        // Special case: input can be assigned to boolean
        if (exprType.equals(SymbolType.Type.PortInput) && declarationType.equals(SymbolType.Type.Boolean)) {
            return;
        }

        // Check if types matches
        if (!exprType.equals(declarationType)) {
            throw new IncompaitbleTypesException(node, declarationType, exprType);
        }
    }
}
