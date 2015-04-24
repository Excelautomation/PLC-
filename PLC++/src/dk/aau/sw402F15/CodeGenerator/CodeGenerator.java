package dk.aau.sw402F15.CodeGenerator;

import dk.aau.sw402F15.parser.analysis.DepthFirstAdapter;
import dk.aau.sw402F15.parser.node.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by Claus on 24-04-2015.
 */
public class CodeGenerator extends DepthFirstAdapter {
    int jumpLabel = 0;

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
    public void caseAAssignmentDeclaration(AAssignmentDeclaration node){
        throw new NotImplementedException();
    }

    @Override
    public void caseAIfElseStatement(AIfElseStatement node){
        int ifLabel = getNextJump();
        int elseLabel = getNextJump();

        node.getCondition().apply(this);
        Emit("CJP " + ifLabel);
        node.getElse().apply(this);
        Emit("JMP " + elseLabel);
        Emit("JME " + ifLabel);
        node.getStatement().apply(this);
        Emit("JME " + elseLabel);
    }

    @Override
    public void caseAIfStatement(AIfStatement node){
        int label = getNextJump();

        node.getCondition().apply(this);
        Emit("CJPN " + label);
        node.getStatement().apply(this);
        Emit("JME " + label);
    }

    @Override
    public void caseAForStatement(AForStatement node){
        int jumpLabel = getNextJump();
        int loopLabel = getNextJump();

        node.getInitilizer().apply(this);
        Emit("JMP " + jumpLabel);
        Emit("JME " + loopLabel);
        node.getStatement().apply(this);
        node.getIterator().apply(this);
        Emit("JME " + jumpLabel);
        node.getCondition().apply(this);
        Emit("CJP " + loopLabel);
    }

    @Override
    public void caseASwitchStatement(ASwitchStatement node){
        throw new NotImplementedException();
    }

    @Override
    public void caseAWhileStatement(AWhileStatement node){
        int jumpLabel = getNextJump();
        int loopLabel = getNextJump();

        Emit("JMP " + jumpLabel);
        Emit("JME " + loopLabel);
        node.getStatement().apply(this);
        Emit("JME " + jumpLabel);
        node.getCondition().apply(this);
        Emit("CJP " + loopLabel);
    }

    private int getNextJump(){
        jumpLabel = jumpLabel + 1;
        if(jumpLabel > 255)
            throw new IndexOutOfBoundsException();
        return jumpLabel;
    }

    private void Emit(String string){
        // Writes to file
        throw new NotImplementedException();
    }
}
