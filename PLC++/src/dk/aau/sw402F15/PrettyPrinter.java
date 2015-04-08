package dk.aau.sw402F15;

import dk.aau.sw402F15.parser.analysis.DepthFirstAdapter;
import dk.aau.sw402F15.parser.node.Node;

/**
 * Created by sahb on 08/04/15.
 */
public class PrettyPrinter extends DepthFirstAdapter {
    private int indent = 0;
    private int cntIndent = 3;

    @Override
    public void defaultIn(@SuppressWarnings("unused") Node node) {
        super.defaultIn(node);

        printIndent();

        System.out.println(node.getClass().getSimpleName());

        indent+=cntIndent;
    }

    @Override
    public void defaultOut(@SuppressWarnings("unused") Node node) {
        super.defaultOut(node);
        indent-=cntIndent;
    }

    private void printIndent() {
        String str = "";
        for (int i = 0; i < indent; i++) {
            str += " ";
        }

        System.out.print(str);
    }
}
