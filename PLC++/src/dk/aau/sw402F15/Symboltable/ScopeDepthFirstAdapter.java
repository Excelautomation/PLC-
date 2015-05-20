package dk.aau.sw402F15.Symboltable;

import dk.aau.sw402F15.parser.analysis.DepthFirstAdapter;
import dk.aau.sw402F15.parser.node.AFunctionRootDeclaration;
import dk.aau.sw402F15.parser.node.AScopeStatement;
import dk.aau.sw402F15.parser.node.AStructRootDeclaration;

/**
 * Created by sahb on 15/04/15.
 */
public class ScopeDepthFirstAdapter extends DepthFirstAdapter {
    protected final Scope rootScope;
    public Scope currentScope;

    public ScopeDepthFirstAdapter(Scope rootScope, Scope currentScope) {
        this.rootScope = rootScope;
        this.currentScope = currentScope;
    }

    @Override
    public void inAScopeStatement(AScopeStatement node) {
        super.inAScopeStatement(node);
        currentScope = currentScope.getSubScopeByNodeOrThrow(node);
    }

    @Override
    public void outAScopeStatement(AScopeStatement node) {
        super.outAScopeStatement(node);
        currentScope = currentScope.getParentScope();
    }

    @Override
    public void inAFunctionRootDeclaration(AFunctionRootDeclaration node) {
        super.inAFunctionRootDeclaration(node);
        currentScope = currentScope.getSubScopeByNodeOrThrow(node);
    }

    @Override
    public void outAFunctionRootDeclaration(AFunctionRootDeclaration node) {
        super.outAFunctionRootDeclaration(node);
        currentScope = currentScope.getParentScope();
    }

    @Override
    public void inAStructRootDeclaration(AStructRootDeclaration node) {
        super.inAStructRootDeclaration(node);
        currentScope = currentScope.getSubScopeByNodeOrThrow(node);
    }

    @Override
    public void outAStructRootDeclaration(AStructRootDeclaration node) {
        super.outAStructRootDeclaration(node);
        currentScope = currentScope.getParentScope();
    }
}
