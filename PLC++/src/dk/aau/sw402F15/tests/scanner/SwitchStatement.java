package dk.aau.sw402F15.tests.scanner;

import dk.aau.sw402F15.parser.lexer.LexerException;
import dk.aau.sw402F15.parser.parser.ParserException;
import dk.aau.sw402F15.tests.ScannerTest;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by jnie on 15/04/15.
 */
public class SwitchStatement extends ScannerTest {
    @Test
    public void switchStatement() throws ParserException, IOException, LexerException {
        checkCode("void run() { switch ( expr ) { case expr : { break; } default : { break; } } }");
    }

    @Test(expected = ParserException.class)
    public void switchWithError() throws ParserException, IOException, LexerException {
        checkCode("void run() { switch ( expr ) { case : } }");
    }

}
