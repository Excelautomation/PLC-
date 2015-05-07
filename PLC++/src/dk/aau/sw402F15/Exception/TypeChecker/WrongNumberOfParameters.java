package dk.aau.sw402F15.Exception.TypeChecker;

import dk.aau.sw402F15.parser.node.Node;

/**
 * Created by sahb on 07/05/15.
 */
public class WrongNumberOfParameters extends TypeCheckerException {
    private int expectedNumbers;
    private int actualNumbers;

    public WrongNumberOfParameters(Node node, int expectedNumbers, int actualNumbers) {
        super(node);

        this.expectedNumbers = expectedNumbers;
        this.actualNumbers = actualNumbers;
    }

    public WrongNumberOfParameters(String message, Node node, int expectedNumbers, int actualNumbers) {
        super(message, node);
        this.expectedNumbers = expectedNumbers;
        this.actualNumbers = actualNumbers;
    }
}
