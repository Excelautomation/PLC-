package dk.aau.sw402F15;

import com.sun.media.jfxmedia.events.PlayerStateEvent;
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

    // TODO This does not really do anything. Just a backup.
    @Override
    public void outASwitchStatement(ASwitchStatement node) {
        List<PStatement> statementList = new ArrayList<PStatement>();

       // Checking if the switch-case contains default
        if (node.getLabels().getLast() instanceof ADefaultStatement)
        {
            /*PStatement firstStatement = node.getLabels().remove();
            PStatement defaultStatement = node.getLabels().removeLast();
            AScopeStatement newScope = new AScopeStatement();

            for (PStatement item : node.getLabels())
            {
                statementList.add(new AIfStatement(new ACompareAndExpr(node.getCondition(), ((ACaseStatement)item).getCase()), ((ACaseStatement)item).getStatement()));
            }*/

            /*AIfElseStatement lulz = new AIfElseStatement(node.getCondition(), new AScopeStatement(statementList), defaultStatement);
            int i = 8;
            AIfElseStatement lulz = new AIfElseStatement(new ACompareEqualExpr(node.getCondition(), ((ACaseStatement)firstStatement).getCase()), new AScopeStatement(statementList),  ((ACaseStatement) node.getLabels().getLast()).getStatement());
            node.replaceBy(lulz);*/

            AIfElseStatement lulz = new AIfElseStatement(new ACompareEqualExpr(node.getCondition(), ((ACaseStatement)node.getLabels().getFirst()).getCase()), ((ACaseStatement)node.getLabels().getFirst()).getStatement(), ((ACaseStatement)node.getLabels().get(1)).getStatement());
            int i = 3;
        }
        else
        {

        }



        super.outASwitchStatement(node);
    }
}