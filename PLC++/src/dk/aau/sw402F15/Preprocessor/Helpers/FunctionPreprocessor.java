package dk.aau.sw402F15.Preprocessor.Helpers;

import dk.aau.sw402F15.Helper;
import dk.aau.sw402F15.Preprocessor.Preprocessor;
import dk.aau.sw402F15.Symboltable.Scope;
import dk.aau.sw402F15.Symboltable.SymbolFunction;
import dk.aau.sw402F15.Symboltable.SymbolType;
import dk.aau.sw402F15.parser.analysis.DepthFirstAdapter;
import dk.aau.sw402F15.parser.node.ADeclaration;
import dk.aau.sw402F15.parser.node.AFunctionRootDeclaration;
import dk.aau.sw402F15.parser.node.PDeclaration;
import dk.aau.sw402F15.parser.node.PStatement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sahb on 22/04/15.
 */
public class FunctionPreprocessor extends DepthFirstAdapter {
    private Scope functionScope;

    public FunctionPreprocessor(Scope functionScope) {
        this.functionScope = functionScope;
    }

    @Override
    public void caseAFunctionRootDeclaration(AFunctionRootDeclaration node) {
        // Get returntype
        SymbolType returnType = Helper.getSymbolTypeFromTypeSpecifier(node.getReturnType());

        // Get formalParameters
        List<SymbolType> formalParameters = new ArrayList<SymbolType>();

        {
            for(PDeclaration e : node.getParams())
            {
                ADeclaration declaration = (ADeclaration)e;
                formalParameters.add(Helper.getSymbolTypeFromTypeSpecifier(declaration.getType()));

                // Apply preprocessor to currentScope
                Preprocessor preprocessor = new Preprocessor(functionScope);
                e.apply(preprocessor);
            }
        }

        // Apply statements to preprocessor
        {
            List<PStatement> copy = new ArrayList<PStatement>(node.getStatements());
            for(PStatement e : copy)
            {
                Preprocessor preprocessor = new Preprocessor(functionScope);
                e.apply(preprocessor);
            }
        }

        functionScope.getParentScope().addSymbol(new SymbolFunction(returnType, formalParameters, node.getName().getText(), node, functionScope));
    }
}
