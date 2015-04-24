package dk.aau.sw402F15.Preprocessor.Helpers;

import dk.aau.sw402F15.Preprocessor.Preprocessor;
import dk.aau.sw402F15.Symboltable.Scope;
import dk.aau.sw402F15.Symboltable.SymbolStruct;
import dk.aau.sw402F15.parser.analysis.DepthFirstAdapter;
import dk.aau.sw402F15.parser.node.AStructRootDeclaration;

/**
 * Created by sahb on 22/04/15.
 */
public class StructPreprocessor extends DepthFirstAdapter {
    private Scope structScope;

    public StructPreprocessor(Scope structScope) {
        this.structScope = structScope;
    }

    @Override
    public void caseAStructRootDeclaration(AStructRootDeclaration node) {
        Preprocessor preprocessor = new Preprocessor(structScope);

        if(node.getProgram() != null)
        {
            node.getProgram().apply(preprocessor);
        }

        String structName = node.getName().getText();
        Scope structScope = preprocessor.getScope();

        // Add symbol to structScope
        Scope rootScope = this.structScope.getParentScope();
        rootScope.addSymbol(new SymbolStruct(structName, structScope.toList(), node, rootScope));
    }
}
