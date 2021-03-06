package dk.aau.sw402F15.tests.scanner;

import dk.aau.sw402F15.parser.lexer.LexerException;
import dk.aau.sw402F15.parser.parser.ParserException;
import dk.aau.sw402F15.tests.ScannerTest;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by sahb on 14/04/15.
 */
public class StructDefinition extends ScannerTest {
    @Test
    public void structTest() throws ParserException, IOException, LexerException {
        checkCode("struct str { int i; int j; float k; }");
    }

    @Test(expected = ParserException.class)
    public void structInStruct() throws ParserException, IOException, LexerException {
        checkCode("struct str { struct str2 { int i; } }");
    }
}
