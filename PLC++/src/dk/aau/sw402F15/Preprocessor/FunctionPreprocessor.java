package dk.aau.sw402F15.Preprocessor;

import dk.aau.sw402F15.Symboltable.Scope;
import dk.aau.sw402F15.parser.analysis.DepthFirstAdapter;

/**
 * Created by sahb on 22/04/15.
 */
public class FunctionPreprocessor extends DepthFirstAdapter {
    private Scope functionScope;

    public FunctionPreprocessor(Scope functionScope) {
        this.functionScope = functionScope;
    }

}
