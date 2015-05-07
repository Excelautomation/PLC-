package dk.aau.sw402F15.Exception;

import dk.aau.sw402F15.parser.analysis.DepthFirstAdapter;
import dk.aau.sw402F15.parser.analysis.ReversedDepthFirstAdapter;
import dk.aau.sw402F15.parser.node.Node;
import dk.aau.sw402F15.parser.node.Start;
import dk.aau.sw402F15.parser.node.Token;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sahb on 07/05/15.
 * http://lists.sablecc.org/pipermail/sablecc-discussion/msg00144.html
 */
public class LineAndPos extends DepthFirstAdapter
{
    private boolean firstSet = false;
    private int startLine = 0, startPos = 0, endLine = 0, endPos = 0;

    @Override
    public void defaultCase(@SuppressWarnings("unused") Node node) {
        if (!(node instanceof Token))
            return;

        Token token = (Token) node;
        if (!firstSet) {
            startLine = token.getLine();
            startPos = token.getPos();

            firstSet = true;
        }

        endLine = token.getLine();
        endPos = token.getPos() + token.getText().length();
    }

    public int getStartLine() {
        return startLine;
    }

    public int getStartPos() {
        return startPos;
    }

    public int getEndLine() {
        return endLine;
    }

    public int getEndPos() {
        return endPos;
    }
}