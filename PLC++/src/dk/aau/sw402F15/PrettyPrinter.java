package dk.aau.sw402F15;

import dk.aau.sw402F15.parser.analysis.DepthFirstAdapter;
import dk.aau.sw402F15.parser.node.ADecimalExpr;
import dk.aau.sw402F15.parser.node.AIdentifierExpr;
import dk.aau.sw402F15.parser.node.AIntegerExpr;
import dk.aau.sw402F15.parser.node.Node;

/**
 * Created by sahb on 08/04/15.
 */
public class PrettyPrinter extends DepthFirstAdapter {
    private int indent = 0;
    private int cntIndent = 3;
    private boolean printNewLine = true;

    @Override
    public void defaultIn(@SuppressWarnings("unused") Node node) {
        super.defaultIn(node);

        printIndent();

        System.out.print(node.getClass().getSimpleName());

        if (printNewLine)
            System.out.println();

        printNewLine = true;

        indent += cntIndent;
    }

    @Override
    public void defaultOut(@SuppressWarnings("unused") Node node) {
        super.defaultOut(node);
        indent -= cntIndent;
    }

    @Override
    public void inAIdentifierExpr(AIdentifierExpr node) {
        printNewLine = false;
        super.inAIdentifierExpr(node);
    }

    @Override
    public void outAIdentifierExpr(AIdentifierExpr node) {
        printExtra(node.getName().getText());
        super.outAIdentifierExpr(node);
    }

    @Override
    public void inADecimalExpr(ADecimalExpr node) {
        printNewLine = false;
        super.inADecimalExpr(node);
    }

    @Override
    public void outADecimalExpr(ADecimalExpr node) {
        printExtra(node.getDecimalLiteral().getText());
        super.outADecimalExpr(node);
    }

    @Override
    public void inAIntegerExpr(AIntegerExpr node) {
        printNewLine = false;
        super.inAIntegerExpr(node);
    }

    @Override
    public void outAIntegerExpr(AIntegerExpr node) {
        printExtra(node.getIntegerLiteral().getText());
        super.outAIntegerExpr(node);
    }

    private void printExtra(String extra) {
        System.out.println(" [" + extra + "]");
    }

    private void printIndent() {
        String str = "";
        for (int i = 0; i < indent; i++) {
            str += " ";
        }

        System.out.print(str);
    }
}
