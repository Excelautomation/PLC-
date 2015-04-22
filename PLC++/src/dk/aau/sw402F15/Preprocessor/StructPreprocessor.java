package dk.aau.sw402F15.Preprocessor;

import dk.aau.sw402F15.Symboltable.Scope;
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
        String structName = node.getName().getText();

        if(node.getName() != null)
        {
            node.getName().apply(this);
        }
        if(node.getProgram() != null)
        {
            node.getProgram().apply(this);
        }

        Preprocessor preprocessor = new Preprocessor(structScope);
    }
}
