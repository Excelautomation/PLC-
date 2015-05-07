package dk.aau.sw402F15.tests.parser;

import dk.aau.sw402F15.parser.node.ABranchStatement;
import dk.aau.sw402F15.parser.node.AFunctionRootDeclaration;
import dk.aau.sw402F15.parser.node.AProgram;
import dk.aau.sw402F15.parser.node.Node;
import dk.aau.sw402F15.tests.ParserTest;
import org.junit.Test;

/**
 * Created by sahb on 19-04-2015.
 */
public class DanglingElse extends ParserTest {
    @Test
    public void danglingIfElseCheck() {
        Node node = getNode("void run() {" +
                "if (true)" +
                "   if (true)" +
                "       a = false;" +
                "   else" +
                "       a = true;" +
                "}");

        // Find first function
        AFunctionRootDeclaration function = getFunction(node);

        // Find if statement
        ABranchStatement ifStatement = (ABranchStatement) function.getStatements().getFirst();

        // Get if-else statement
        ABranchStatement ifElseStatement = (ABranchStatement) ifStatement.getLeft();
    }

    private AFunctionRootDeclaration getFunction(Node node) {
        // Check if node is Abstract syntax program
        AProgram program = (AProgram) node;

        // Find first function
        return (AFunctionRootDeclaration) program.getRootDeclaration().getFirst();
    }

}
