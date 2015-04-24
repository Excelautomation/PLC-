package dk.aau.sw402F15.SymbolProcessor;

import dk.aau.sw402F15.Helper;
import dk.aau.sw402F15.Symboltable.Scope;
import dk.aau.sw402F15.Symboltable.SymbolArray;
import dk.aau.sw402F15.Symboltable.SymbolType;
import dk.aau.sw402F15.Symboltable.SymbolVariable;
import dk.aau.sw402F15.parser.analysis.DepthFirstAdapter;
import dk.aau.sw402F15.parser.node.*;

/**
 * Created by sahb on 22/04/15.
 */
public class DeclarationSymbolProcessor extends DepthFirstAdapter {
    private Scope currentScope;
    private boolean variableIsConst;
    private boolean variableIsArray;

    public DeclarationSymbolProcessor(Scope currentScope) {
        this.currentScope = currentScope;
    }

    @Override
    public void caseADeclaration(ADeclaration node) {
        // Apply qualifier and array
        if(node.getQuailifer() != null)
        {
            node.getQuailifer().apply(this);
        }
        if(node.getArray() != null)
        {
            node.getArray().apply(this);
        }

        // Get type
        SymbolType variableType = Helper.getSymbolTypeFromTypeSpecifier(node.getType());

        // Declare variable
        declareVariable(node, node.getName().getText(), variableType, variableIsConst, variableIsArray);
    }

    @Override
    public void outAConstTypeQualifier(AConstTypeQualifier node) {
        super.outAConstTypeQualifier(node);

        variableIsConst = true;
    }

    @Override
    public void outAArrayDefinition(AArrayDefinition node) {
        super.outAArrayDefinition(node);

        variableIsArray = true;
    }

    private void declareVariable(Node node, String name, SymbolType variableType, boolean isConst, boolean isArray) {
        if (isArray) {
            currentScope.addSymbol(new SymbolArray(variableType, name, node, currentScope));
        }
        else {
            currentScope.addSymbol(new SymbolVariable(variableType, name, node, currentScope, isConst));
        }
    }
}
