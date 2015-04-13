package dk.aau.sw402F15.tests.scanner.declaration;

import dk.aau.sw402F15.parser.lexer.Lexer;
import dk.aau.sw402F15.parser.lexer.LexerException;
import dk.aau.sw402F15.parser.parser.Parser;
import dk.aau.sw402F15.parser.parser.ParserException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.StringReader;
import java.util.Arrays;

/**
 * Created by sahb on 17/03/15.
 */
@RunWith(Parameterized.class)
public class Declaration {

    @Parameterized.Parameters
    public static Iterable<? extends Object> data() {
        return Arrays.asList("bool", "char", "double", "float", "int", "long", "port", "timer");
    }

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

