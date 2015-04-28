package dk.aau.sw402F15.CodeGenerator;

import dk.aau.sw402F15.parser.node.*;

/**
 * Created by mads on 28/04/15.
 */
public class ExprCodeEvaluator extends CodeGenerator {
    public ExprCodeEvaluator() {
        Emit("SSET(630) W0 5");
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

        pushToStack();
        Emit("+(400) r1 r2 r1");
    }

    @Override
    public void outADivExpr(ADivExpr node) {
        super.outADivExpr(node);

        pushToStack();
        Emit("/(430) r1 r2 r1");
    }

    @Override
    public void outAMultiExpr(AMultiExpr node) {
        super.outAMultiExpr(node);
        pushToStack();
        Emit("*(420) r1 r2 r1");
    }

    @Override
    public void outASubExpr(ASubExpr node) {
        super.outASubExpr(node);

        pushToStack();
        Emit("-(410) r1 r2 r1");
    }

    private void pushToStack(){
        Emit("r1 INT W4 0");
        Emit("r2 INT W5 0");

        Emit("LIFO(634) W0 r1");
        Emit("LIFO(634) W0 r2");
    }
}
