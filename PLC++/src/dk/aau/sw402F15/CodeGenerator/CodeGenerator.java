package dk.aau.sw402F15.CodeGenerator;

import dk.aau.sw402F15.parser.analysis.DepthFirstAdapter;
import dk.aau.sw402F15.parser.node.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by Claus on 24-04-2015.
 */
public class CodeGenerator extends DepthFirstAdapter {

    @Override
    public void caseAAddExpr(AAddExpr node){
        throw new NotImplementedException();
    }

    @Override
    public void caseAArrayDefinition(AArrayDefinition node){
        throw new NotImplementedException();
    }

    @Override
    public void caseAArrayExpr(AArrayExpr node){
        throw new NotImplementedException();
    }

    @Override
    public void caseAArrayExpr(AAssignmentDeclaration node){
        throw new NotImplementedException();
    }

    private void Emit(String string){
        // Writes to file
        throw new NotImplementedException();
    }
}
