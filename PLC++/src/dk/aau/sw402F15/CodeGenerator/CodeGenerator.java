package dk.aau.sw402F15.CodeGenerator;

import dk.aau.sw402F15.parser.analysis.DepthFirstAdapter;
import dk.aau.sw402F15.parser.node.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Claus on 24-04-2015.
 */
public class CodeGenerator extends DepthFirstAdapter {
    int jumpLabel = 0;

    PrintWriter writer;

    public CodeGenerator() {
        try {
            writer = new PrintWriter("InstructionList.txt", "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
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
    public void caseADeclaration(ADeclaration node) {
        super.caseADeclaration(node);
    }

    @Override
    public void caseAExpr(AExpr node) {
        ExprCodeEvaluator exprCodeEvaluator = new ExprCodeEvaluator();
        node.apply(exprCodeEvaluator);
    }

    @Override
    public void caseABranchStatement(ABranchStatement node) {
        super.caseABranchStatement(node);

        if (node.getRight() != null) {
            // If - else statement
            int ifLabel = getNextJump();
            int elseLabel = getNextJump();

            node.getCondition().apply(this);
            Emit("CJP " + ifLabel);
            node.getRight().apply(this);
            Emit("JMP " + elseLabel);
            Emit("JME " + ifLabel);
            node.getLeft().apply(this);
            Emit("JME " + elseLabel);
        }
        else {
            // If statement
            int label = getNextJump();

            node.getCondition().apply(this);
            Emit("CJPN " + label);
            node.getLeft().apply(this);
            Emit("JME " + label);
        }
    }

    @Override
    public void caseAForStatement(AForStatement node){
        int jumpLabel = getNextJump();
        int loopLabel = getNextJump();

        {
            List<PExpr> copy = new ArrayList<PExpr>(node.getInitilizer());
            for(PExpr e : copy)
            {
                e.apply(this);
            }
        }
        Emit("JMP " + jumpLabel);
        Emit("JME " + loopLabel);
        node.getStatement().apply(this);
        {
            List<PExpr> copy = new ArrayList<PExpr>(node.getIterator());
            for(PExpr e : copy)
            {
                e.apply(this);
            }
        }
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

    @Override
    public void outAIntegerExpr(AIntegerExpr node) {
        super.outAIntegerExpr(node);

        Emit("PUSH(632) W0 " + node.getIntegerLiteral().getText());
    }

    @Override
    public void outADecimalExpr(ADecimalExpr node) {
        super.outADecimalExpr(node);

        Emit("PUSH(632) W0 " + node.getDecimalLiteral().getText());
    }

    @Override
    public void outAAddExpr(AAddExpr node) {
        super.outAAddExpr(node);

        PopFromStack();
        Emit("+(400) r1 r2 r1");
    }

    @Override
    public void outADivExpr(ADivExpr node) {
        super.outADivExpr(node);

        PopFromStack();
        Emit("/(430) r1 r2 r1");
    }

    @Override
    public void outAMultiExpr(AMultiExpr node) {
        super.outAMultiExpr(node);
        PopFromStack();
        Emit("*(420) r1 r2 r1");
    }

    @Override
    public void outASubExpr(ASubExpr node) {
        super.outASubExpr(node);

        PopFromStack();
        Emit("-(410) r1 r2 r1");
    }

    private void PopFromStack(){
        Emit("r1 INT W4 0");
        Emit("r2 INT W5 0");

        Emit("LIFO(634) W0 r1");
        Emit("LIFO(634) W0 r2");
    }

    protected void Emit(String inst){

        writer.println(inst);

    }
}
