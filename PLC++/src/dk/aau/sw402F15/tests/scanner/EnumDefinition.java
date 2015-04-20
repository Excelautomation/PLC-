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
    public void enumTest() throws ParserException, IOException, LexerException {
        checkCode("enum weekday { Monday = 1, Tuesday = 2, Wednesday = 3, Thursday = 4, Friday = 5, Saturday = 6, Sunday = 7 }");
    }

    @Test(expected = ParserException.class)
    public void enumWithError() throws ParserException, IOException, LexerException {
        checkCode("enum weekday { Monday = 1, Tuesday = 2: Wednesday = 3, Thursday = 4, Friday = 5, Saturday = 6, Sunday = 7 }");
    }
}
