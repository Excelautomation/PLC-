package dk.aau.sw402F15.CodeGenerator;

import dk.aau.sw402F15.parser.node.*;

/**
 * Created by mads on 28/04/15.
 */
public class ExprCodeEvaluator extends CodeGenerator {
    @Override
    public void outAAddExpr(AAddExpr node) {
        super.outAAddExpr(node);

        Emit("");
    }

    @Override
    public void outADivExpr(ADivExpr node) {
        super.outADivExpr(node);
    }

    @Override
    public void outACompoundMultExpr(ACompoundMultExpr node) {
        super.outACompoundMultExpr(node);
    }

    @Override
    public void outAModExpr(AModExpr node) {
        super.outAModExpr(node);
    }

    @Override
    public void outAMultiExpr(AMultiExpr node) {
        super.outAMultiExpr(node);
    }

    @Override
    public void outASubExpr(ASubExpr node) {
        super.outASubExpr(node);
    }
}
