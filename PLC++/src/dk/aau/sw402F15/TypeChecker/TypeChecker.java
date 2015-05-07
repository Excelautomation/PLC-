package dk.aau.sw402F15.TypeChecker;

import dk.aau.sw402F15.Helper;
import dk.aau.sw402F15.Symboltable.Scope;
import dk.aau.sw402F15.Symboltable.ScopeDepthFirstAdapter;
import dk.aau.sw402F15.parser.node.ADeclarationRootDeclaration;
import dk.aau.sw402F15.parser.node.AFunctionRootDeclaration;
import dk.aau.sw402F15.parser.node.AVoidTypeSpecifier;

/**
 * Created by sahb on 27/04/15.
 */
public class TypeChecker extends ScopeDepthFirstAdapter {

    public TypeChecker(Scope rootScope) {
        this(rootScope, rootScope);
    }

    public TypeChecker(Scope rootScope, Scope currentScope) {
        super(rootScope, currentScope);
    }

    @Override
    public void caseAFunctionRootDeclaration(AFunctionRootDeclaration node) {
        FunctionTypeChecker functionTypeChecker = new FunctionTypeChecker(
                rootScope,
                currentScope,
                node.getReturnType().getClass() != AVoidTypeSpecifier.class,
                Helper.getSymbolTypeFromTypeSpecifier(node.getReturnType())
        );
        node.apply(functionTypeChecker);
    }

    @Override
    public void caseADeclarationRootDeclaration(ADeclarationRootDeclaration node) {
        DeclarationTypeChecker declarationTypeChecker = new DeclarationTypeChecker(currentScope);
        node.apply(declarationTypeChecker);
    }

    public Scope getScope() {
        return rootScope;
    }
}
