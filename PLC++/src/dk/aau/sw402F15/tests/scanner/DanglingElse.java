package dk.aau.sw402F15.tests.scanner;

import dk.aau.sw402F15.parser.lexer.LexerException;
import dk.aau.sw402F15.parser.parser.ParserException;
import dk.aau.sw402F15.tests.ScannerTest;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by sahb on 19-04-2015.
 */
public class DanglingElse extends ScannerTest {
    @Test
    public void checkDanglingIfElse() throws ParserException, IOException, LexerException {
        checkCode("void run() {" +
                "if (true)" +
                "   if (true)" +
                "       a = false;" +
                "   else" +
                "       a = true;" +
                "}");
    }

    @Test
    public void checkDanglingIfElseComplete() throws ParserException, IOException, LexerException {
        checkCode("void run() {" +
                "if (true)" +
                "   if (true)" +
                "       a = false;" +
                "   else" +
                "       a = true;" +
                "else" +
                "   if (false)" +
                "       a = true;" +
                "   else" +
                "       a = false;" +
                "}");
    }

    @Test
    public void checkDanglingIfElseInWhileIteration() throws ParserException, IOException, LexerException {
        checkCode("void run() {" +
                "while (true)" +
                "   if (true)" +
                "       if (true)" +
                "           a = false;" +
                "       else" +
                "           a = true;" +
                "}");
    }
}
