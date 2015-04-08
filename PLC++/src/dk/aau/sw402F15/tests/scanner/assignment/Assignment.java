package dk.aau.sw402F15.tests.scanner.assignment;

import dk.aau.sw402F15.parser.lexer.Lexer;
import dk.aau.sw402F15.parser.lexer.LexerException;
import dk.aau.sw402F15.parser.parser.Parser;
import dk.aau.sw402F15.parser.parser.ParserException;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.StringReader;

/**
 * Created by sahb on 08/04/15.
 */
public class Assignment {
    private void checkCode(String code) throws ParserException, IOException, LexerException {
        getParser(code).parse();
    }

    private Parser getParser(String code) {
        return new Parser(new Lexer(new PushbackReader(new StringReader(code), 1024)));
    }
}
