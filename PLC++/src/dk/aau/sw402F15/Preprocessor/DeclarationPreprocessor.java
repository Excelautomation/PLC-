package dk.aau.sw402F15.Preprocessor;

import dk.aau.sw402F15.Symboltable.Scope;
import dk.aau.sw402F15.parser.analysis.DepthFirstAdapter;

/**
 * Created by sahb on 22/04/15.
 */
public class DeclarationPreprocessor extends DepthFirstAdapter {
    private Scope currentScope;

    public DeclarationPreprocessor(Scope currentScope) {
        this.currentScope = currentScope;
    }
}
