package dk.aau.sw402F15.tests.scanner.declaration;

import dk.aau.sw402F15.parser.lexer.Lexer;
import dk.aau.sw402F15.parser.lexer.LexerException;
import dk.aau.sw402F15.parser.parser.Parser;
import dk.aau.sw402F15.parser.parser.ParserException;
import org.junit.Test;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.StringReader;

/**
 * Created by sahb on 17/03/15.
 */
public abstract class Declaration {

    private String type;

    public Declaration(String type) {
        this.type = type;
    }

    @Test
    public void Declaration() throws ParserException, IOException, LexerException {
        checkCode(type + " variable;");
    }

    @Test
    public void DeclarationAssignment() throws ParserException, IOException, LexerException {
        checkCode(type + " variable = 1;");
    }

    private void checkCode(String code) throws ParserException, IOException, LexerException {
        getParser(code).parse();
    }

    private Parser getParser(String code) {
        return new Parser(new Lexer(new PushbackReader(new StringReader(code), 1024)));
    }
}

