package dk.aau.sw402F15.tests.scanner;

import dk.aau.sw402F15.parser.lexer.LexerException;
import dk.aau.sw402F15.parser.parser.ParserException;
import dk.aau.sw402F15.tests.ScannerTest;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by sahb on 14/04/15.
 */
public class WhileIteration extends ScannerTest {
    @Test
    public void whileIteration() throws ParserException, IOException, LexerException {
        checkCode("void run() { while(true) { i = i; } }");
    }

    @Test
    public void whileExpr() throws ParserException, IOException, LexerException {
        checkCode("void run() { while(i < 10) { i = ++i; } }");
    }
}
