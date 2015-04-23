package dk.aau.sw402F15;

import dk.aau.sw402F15.parser.analysis.DepthFirstAdapter;
import dk.aau.sw402F15.parser.node.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Mikkel on 22-04-2015.
 */
public class ASTSimplify extends DepthFirstAdapter {

    @Override
    public void outAForStatement(AForStatement node) {
        List<PStatement> statementList = new ArrayList<PStatement>();

        Node parent = node.parent();

        statementList.add(new AExprStatement(node.getInitilizer()));
        statementList.add(new AWhileStatement(node.getCondition(), node.getStatement()));
        statementList.add(new AExprStatement(node.getIterator())); // TODO Need to add iterator to the previous while-statement

        /*statementList.add(new AExprStatement(node.getIterator()));
        statementList.add(new AExprStatement(node.getCondition()));*/

        statementList.isEmpty();

        super.outAForStatement(node);
    }
}
