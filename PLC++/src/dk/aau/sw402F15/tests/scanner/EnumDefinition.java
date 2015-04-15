package dk.aau.sw402F15.tests.scanner;

import dk.aau.sw402F15.parser.lexer.LexerException;
import dk.aau.sw402F15.parser.parser.ParserException;
import dk.aau.sw402F15.tests.ScannerTest;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by jnie on 15/04/15.
 */
public class EnumDefinition extends ScannerTest {
    @Test
    public void StructTest() throws ParserException, IOException, LexerException {
        checkCode("enum str { int i; int j; float k; }");
    }
}
