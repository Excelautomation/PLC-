package dk.aau.sw402F15.ScopeChecker;
import dk.aau.sw402F15.TypeChecker.Symboltable.Scope;
import dk.aau.sw402F15.TypeChecker.Symboltable.Symbol;
import dk.aau.sw402F15.TypeChecker.Symboltable.SymbolFunction;
import dk.aau.sw402F15.parser.analysis.DepthFirstAdapter;
import dk.aau.sw402F15.parser.node.ABoolType;
import dk.aau.sw402F15.parser.node.AFunctionFunctionDeclaration;
import dk.aau.sw402F15.parser.node.AVoidFunctionFunctionDeclaration;

import java.util.Stack;

/**
 * Created by Mads on 08/04/15.
 */

public class ScopeChecker extends DepthFirstAdapter {
    private Scope rootScope = new Scope(null);
    private Scope currentScope;

    Stack<Object> stack = new Stack<Object>();

    public ScopeChecker() {
        currentScope = rootScope;
    }



    @Override
    public void outAFunctionFunctionDeclaration(AFunctionFunctionDeclaration node) {
        super.outAFunctionFunctionDeclaration(node);

        //currentScope.addSymbol(new SymbolFunction(node));
    }

    @Override
    public void outAVoidFunctionFunctionDeclaration(AVoidFunctionFunctionDeclaration node) {
        super.outAVoidFunctionFunctionDeclaration(node);

    }

    @Override
    public void outABoolType(ABoolType node) {
        super.outABoolType(node);
        stack.push(node);
        // use list
    }
}
