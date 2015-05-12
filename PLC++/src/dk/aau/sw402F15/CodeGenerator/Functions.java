package dk.aau.sw402F15.CodeGenerator;

import dk.aau.sw402F15.Symboltable.Scope;
import dk.aau.sw402F15.Symboltable.ScopeDepthFirstAdapter;
import dk.aau.sw402F15.parser.analysis.DepthFirstAdapter;
import dk.aau.sw402F15.parser.node.AFunctionRootDeclaration;
import dk.aau.sw402F15.tests.scopechecker.Function;

import java.util.ArrayList;

/**
 * Created by Mikkel on 12-05-2015.
 */
public class Functions extends ScopeDepthFirstAdapter {

    private int number = 0;

    public int getNext(){
        return number++;
    }

    public ArrayList<String> functions = new ArrayList<String>();

    @Override
    public void inAFunctionRootDeclaration(AFunctionRootDeclaration node) {
        super.inAFunctionRootDeclaration(node);
        functions.add(getNext(), node.getName().getText());
    }

    public Functions(Scope scope)
    {
        super(scope, scope);
    }
}
