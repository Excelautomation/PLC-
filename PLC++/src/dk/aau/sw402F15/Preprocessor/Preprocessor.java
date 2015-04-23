package dk.aau.sw402F15.Preprocessor;

import dk.aau.sw402F15.Exception.CompilerInternalException;
import dk.aau.sw402F15.Symboltable.Scope;
import dk.aau.sw402F15.parser.analysis.DepthFirstAdapter;
import dk.aau.sw402F15.parser.node.*;

/**
 * Created by sahb on 22/04/15.
 */
public class Preprocessor extends DepthFirstAdapter {
    private Scope currentScope;

    public Preprocessor() {
        this(new Scope(null, null));
    }

    public Preprocessor(Scope currentScope) {
        this.currentScope = currentScope;
    }

    public Scope getScope() {
        return currentScope;
    }

    // Implement program since we give an error if calling defaultIn
    @Override
    public void caseAProgram(AProgram node) {
        for (PRootDeclaration root : node.getRootDeclaration()) {
            root.apply(this);
        }
    }

    @Override
    public void caseAFunctionRootDeclaration(AFunctionRootDeclaration node) {
        FunctionPreprocessor functionPreprocessor = new FunctionPreprocessor(currentScope.addSubScope(node));
        node.apply(functionPreprocessor);
    }

    @Override
    public void caseAStructRootDeclaration(AStructRootDeclaration node) {
        StructPreprocessor structPreprocessor = new StructPreprocessor(currentScope.addSubScope(node));
        node.apply(structPreprocessor);
    }

    @Override
    public void caseAEnumRootDeclaration(AEnumRootDeclaration node) {
        throw new CompilerInternalException("Not implemented");
    }

    @Override
    public void caseADeclarationRootDeclaration(ADeclarationRootDeclaration node) {
        DeclarationPreprocessor declarationPreprocessor = new DeclarationPreprocessor(currentScope);
        node.apply(declarationPreprocessor);
    }
}
