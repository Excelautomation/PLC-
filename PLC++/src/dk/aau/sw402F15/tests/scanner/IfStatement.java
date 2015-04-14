package dk.aau.sw402F15.tests.scanner;

import dk.aau.sw402F15.parser.lexer.LexerException;
import dk.aau.sw402F15.parser.parser.ParserException;
import dk.aau.sw402F15.tests.ScannerTest;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by sahb on 14/04/15.
 */
public class IfStatement extends ScannerTest {
    @Test
    public void ifStatement() throws ParserException, IOException, LexerException {
        checkCode("void run() { if (true) { } }");
    }

    @Test
    public void ifElseStatement() throws ParserException, IOException, LexerException {
        checkCode("void run() { if (true) { } else { } }");
    }

    @Test
    public void ifIfElseStatement() throws ParserException, IOException, LexerException {
        checkCode("void run() { if (true) { } else if (false) { } else if (true) { } else { } }");
    }

}
