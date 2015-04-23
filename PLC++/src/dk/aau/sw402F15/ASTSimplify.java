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
        List<PStatement> statements = new ArrayList<PStatement>();

        statements.add(node.getStatement());
        statements.add(new AExprStatement(node.getIterator()));

        statementList.add(new AExprStatement(node.getInitilizer()));
        statementList.add(new AWhileStatement(node.getCondition(), new AScopeStatement(statements)));

        node.replaceBy(new AScopeStatement(statementList));

        super.outAForStatement(node);
    }

}