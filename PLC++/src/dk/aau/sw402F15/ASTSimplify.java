package dk.aau.sw402F15;

import dk.aau.sw402F15.parser.analysis.DepthFirstAdapter;
import dk.aau.sw402F15.parser.node.AExprStatement;
import dk.aau.sw402F15.parser.node.AForStatement;
import dk.aau.sw402F15.parser.node.PStatement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mikkel on 22-04-2015.
 */
public class ASTSimplify extends DepthFirstAdapter {

    @Override
    public void outAForStatement(AForStatement node) {
        List<PStatement> statementList = new ArrayList<PStatement>();

        statementList.add(new AExprStatement(node.getInitilizer()));

        super.outAForStatement(node);
    }
}
