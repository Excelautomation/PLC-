package dk.aau.sw402F15.TypeChecker;

import dk.aau.sw402F15.ScopeChecker.MemberChecker;
import dk.aau.sw402F15.Symboltable.Scope;
import dk.aau.sw402F15.Symboltable.Symbol;
import dk.aau.sw402F15.Symboltable.Type.SymbolType;
import dk.aau.sw402F15.parser.analysis.DepthFirstAdapter;
import dk.aau.sw402F15.parser.node.AMemberExpr;

/**
 * Created by sahb on 27/04/15.
 */
public class MemberExpressionEvaluator extends DepthFirstAdapter {
    private Scope scope;
    private SymbolType symbol;

    public MemberExpressionEvaluator(Scope scope) {
        this.scope = scope;
    }

    @Override
    public void caseAMemberExpr(AMemberExpr node) {
        MemberChecker memberChecker = new MemberChecker(scope);
        node.apply(memberChecker);

        symbol = memberChecker.getSymbol();
    }

    public SymbolType getSymbol() {
        return symbol;
    }
}
