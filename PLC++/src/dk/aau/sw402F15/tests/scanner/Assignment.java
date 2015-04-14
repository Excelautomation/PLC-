package dk.aau.sw402F15.tests.scanner;

import dk.aau.sw402F15.parser.lexer.LexerException;
import dk.aau.sw402F15.parser.parser.ParserException;
import dk.aau.sw402F15.tests.ScannerTest;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by sahb on 14/04/15.
 */
public class Assignment extends ScannerTest {
    @Test
    public void checkIntegerAssignment() throws ParserException, IOException, LexerException {
        checkCode("void run() { i = 1; }");
    }

    @Test
    public void checkDecimalAssignment() throws ParserException, IOException, LexerException {
        checkCode("void run() { i = 1.1; }");
    }

    @Test(expected = ParserException.class)
    public void checkSemiRequired() throws ParserException, IOException, LexerException {
        checkCode("void run() { i = 1 }");
    }

    @Test(expected = ParserException.class)
    public void checkMethodOrFunctionRequired() throws ParserException, IOException, LexerException {
        checkCode("i = 1;");
    }

    @Test
    public void checkAssignmentInAssignment() throws ParserException, IOException, LexerException {
        checkCode("void run() { i = j = 1; }");
    }

    @Test
    public void checkAssignmentIdentifier() throws ParserException, IOException, LexerException {
        checkCode("void run() { i = j; }");
    }

    @Test
    public void checkBoolAssignment() throws ParserException, IOException, LexerException {
        checkCode("void run() { i = true; }");
        checkCode("void run() { i = false; }");
    }
}
