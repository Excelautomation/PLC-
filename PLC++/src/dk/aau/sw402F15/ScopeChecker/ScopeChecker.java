package dk.aau.sw402F15.ScopeChecker;

import dk.aau.sw402F15.TypeChecker.Exceptions.SymbolFoundWrongTypeException;
import dk.aau.sw402F15.TypeChecker.Exceptions.SymbolNotFoundException;
import dk.aau.sw402F15.Symboltable.*;
import dk.aau.sw402F15.parser.analysis.DepthFirstAdapter;
import dk.aau.sw402F15.parser.node.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Mads on 08/04/15.
 *
 */

public class ScopeChecker extends ScopeDepthFirstAdapter {
    public ScopeChecker(Scope rootScope) {
        this(rootScope, rootScope);
    }

    public ScopeChecker(Scope rootScope, Scope currentScope) {
        super(rootScope, currentScope);
    }

    @Override
    public void caseTIdentifier(TIdentifier node)
    {
        currentScope.getSymbolOrThrow(node.getText());
    }

    @Override
    public void caseAMemberExpr(AMemberExpr node) {
        MemberChecker memberChecker = new MemberChecker(currentScope);
        node.apply(memberChecker);
    }

    // Returns the outermost scope as the symbol table.
    public Scope getSymbolTable() {
        return rootScope;
    }
}