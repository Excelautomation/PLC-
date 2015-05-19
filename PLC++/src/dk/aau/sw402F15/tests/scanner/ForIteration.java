package dk.aau.sw402F15.tests.scanner;

import dk.aau.sw402F15.parser.lexer.LexerException;
import dk.aau.sw402F15.parser.parser.ParserException;
import dk.aau.sw402F15.tests.ScannerTest;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by sahb on 14/04/15.
 */
public class ForIteration extends ScannerTest {
    @Test
    public void forIteration() throws ParserException, IOException, LexerException {
        checkCode("void run() { for (i = 0; i < 10; i++) { } }");
    }

    @Test
    public void forIterationWithoutParams() throws ParserException, IOException, LexerException {
        checkCode("void run() { for (;;) { } }");
    }

    @Test(expected = ParserException.class)
    // Check that loops can't exist outside a function
    public void forIterationInRoot() throws ParserException, IOException, LexerException {
        checkCode("for (;;) { }");
    }
}
