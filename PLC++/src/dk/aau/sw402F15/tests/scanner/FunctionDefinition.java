package dk.aau.sw402F15.tests.scanner;

import dk.aau.sw402F15.parser.lexer.LexerException;
import dk.aau.sw402F15.parser.parser.ParserException;
import dk.aau.sw402F15.tests.ScannerTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by sahb on 08/04/15.
 */
@RunWith(Parameterized.class)
public class FunctionDefinition extends ScannerTest {

    @Parameterized.Parameters
    public static Iterable<? extends Object> data() {
        return Arrays.asList("void", "bool", "char", "double", "float", "int", "long", "input", "output", "timer");
    }

    private String type;

    public FunctionDefinition(String type) {
        this.type = type;
    }

    @Test
    public void testFunctionDefinition() throws ParserException, IOException, LexerException {
        checkCode(type + " func() { }");
    }

    @Test
    public void testFunctionWithSingleFormalParameter() throws ParserException, IOException, LexerException {
        checkCode(type + " func(int i) { }");
    }

    @Test
    public void testFunctionWithMultipleFormalParameters() throws ParserException, IOException, LexerException {
        checkCode(type + " func(int i, float j) { }");
        checkCode(type + " func(int i, float j, char k) { }");
    }

    @Test(expected = ParserException.class)
    public void testFunctionWithVoidFormalParameter() throws ParserException, IOException, LexerException {
        checkCode(type + " func(void v) { }");
    }
}
