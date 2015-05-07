package dk.aau.sw402F15.Exception;

import dk.aau.sw402F15.parser.node.Node;

import java.util.ArrayList;

/**
 * Created by sahb on 22/04/15.
 */
public abstract class CompilerException extends RuntimeException {
    private Node node;

    public void printError(String code) {
        LineAndPos lineAndPos = new LineAndPos();
        node.apply(lineAndPos);

        System.err.println(getMessage() + ":");
        System.err.println("Linje: " + (lineAndPos.getStartLine() == lineAndPos.getEndLine() ? lineAndPos.getStartLine() : (lineAndPos.getStartLine() + "-" + lineAndPos.getEndLine())));
        System.err.println("Position: " + lineAndPos.getStartPos() + "-" + lineAndPos.getEndPos());

        printCode(code, lineAndPos.getStartLine(), lineAndPos.getEndLine(), lineAndPos.getStartPos(), lineAndPos.getEndPos());
    }

    void printCode(String code, int startLine, int endLine, int startPos, int endPos) {
        // Get lines
        String[] lines = code.split("\n");

        // Lines filter
        {
            // Take only required lines
            ArrayList<String> filteredLines = new ArrayList<String>();
            for (int i = startLine - 1; i < endLine; i++) {
                filteredLines.add(lines[i]);
            }

            // Transfer to array
            lines = new String[endLine - startLine + 1];
            filteredLines.toArray(lines);
        }

        // Position filter
        if (startLine == endLine) {
            lines[0] = lines[0].substring(startPos - 1, endPos - 1);
        } else {
            lines[0] = lines[0].substring(startPos - 1);
            lines[lines.length - 1].substring(0, endPos - 1);
        }

        String output = "";
        for (String line : lines) {
            if (!output.equals(""))
                output += "\n";

            output += line;
        }

        System.err.println(output);

    }

    public CompilerException(String message, Node node) {
        super(message);
        this.node = node;
    }
}


