package dk.aau.sw402F15.ScopeChecker;

import dk.aau.sw402F15.SymbolProcessor.DeclarationSymbolProcessor;
import dk.aau.sw402F15.Symboltable.Scope;
import dk.aau.sw402F15.Symboltable.ScopeDepthFirstAdapter;
import dk.aau.sw402F15.parser.node.ADeclaration;
import dk.aau.sw402F15.parser.node.ADeclarationRootDeclaration;
import dk.aau.sw402F15.parser.node.AMemberExpr;
import dk.aau.sw402F15.parser.node.TIdentifier;

/**
 * Created by Mads on 08/04/15.
 */

public class ScopeChecker extends ScopeDepthFirstAdapter {
    public ScopeChecker(Scope rootScope) {
        this(rootScope, rootScope);
    }

    public ScopeChecker(Scope rootScope, Scope currentScope) {
        super(rootScope, currentScope);
    }

    // Ignore declaration root declaration since we are running the preprocessor for root declaration in preprocessor
    @Override
    public void caseADeclarationRootDeclaration(ADeclarationRootDeclaration node) {
        //super.caseADeclarationRootDeclaration(node);
    }

    @Override
    public void inADeclaration(ADeclaration node) {
        super.inADeclaration(node);

        // Run symbol processor for declarations which are not in root scope (see above comment)
        // It needs to be in the scopechecker because a symbol not in rootscope has to be declared before it's used
        DeclarationSymbolProcessor declarationPreprocessor = new DeclarationSymbolProcessor(currentScope);
        node.apply(declarationPreprocessor);
    }

    // Check all symbols
    @Override
    public void caseTIdentifier(TIdentifier node) {
        currentScope.getSymbolOrThrow(node.getText(), node);
    }

    @Override
    public void caseAMemberExpr(AMemberExpr node) {
        // Don't call super method since the method caseTIdentifier will check each part of the member expression
        MemberChecker memberChecker = new MemberChecker(currentScope);
        node.apply(memberChecker);
    }

    // Returns the outermost scope as the symbol table.
    public Scope getSymbolTable() {
        return rootScope;
    }
}