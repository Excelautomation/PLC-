package dk.aau.sw402F15.Preprocessor;

import dk.aau.sw402F15.Exception.CompilerInternalException;
import dk.aau.sw402F15.Symboltable.Scope;
import dk.aau.sw402F15.parser.analysis.DepthFirstAdapter;
import dk.aau.sw402F15.parser.node.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by sahb on 22/04/15.
 */
public class Preprocessor extends DepthFirstAdapter {
    private Scope currentScope;

    public Preprocessor() {
        currentScope = new Scope(null, null);
    }

    public Preprocessor(Scope currentScope) {
        currentScope = currentScope;
    }

    @Override
    public void defaultIn(@SuppressWarnings("unused") Node node) {
        // Default statement should never be reached since we override case
        // TODO: New exception
        throw new CompilerInternalException("Preprocessor illegal statement");
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
