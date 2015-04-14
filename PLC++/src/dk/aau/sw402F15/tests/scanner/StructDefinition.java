package dk.aau.sw402F15.tests.scanner;

import dk.aau.sw402F15.parser.lexer.LexerException;
import dk.aau.sw402F15.parser.parser.ParserException;
import dk.aau.sw402F15.tests.AbstractTest;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by sahb on 14/04/15.
 */
public class StructDefinition extends AbstractTest {
    @Test
    public void StructTest() throws ParserException, IOException, LexerException {
        checkCode("struct str { int i; int j; float k; }");
    }
}
