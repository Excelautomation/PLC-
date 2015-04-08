package dk.aau.sw402F15.tests.typechecker;

import dk.aau.sw402F15.TypeChecker.Exceptions.IllegalAssignment;
import dk.aau.sw402F15.TypeChecker.TypeChecker;
import dk.aau.sw402F15.parser.lexer.Lexer;
import dk.aau.sw402F15.parser.lexer.LexerException;
import dk.aau.sw402F15.parser.node.Start;
import dk.aau.sw402F15.parser.parser.Parser;
import dk.aau.sw402F15.parser.parser.ParserException;
import org.junit.Test;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.StringReader;

/**
 * Created by sahb on 08/04/15.
 */
public class TypeCheckerTests {

    @Test(expected = IllegalAssignment.class)
    public void checkIntDeclarationSetToBool() {
        checkCode("int i = true;");
    }

    @Test
    public void checkIntDeclaration(){
        checkCode("int i = 0;");
    }

    @Test
    public void checkBoolDeclaration() {
        checkCode("bool b = true;");
    }

    @Test(expected = IllegalAssignment.class)
    public void checkBoolDeclarationSetToInt() {
        checkCode("bool b = 1;");
    }

    @Test(expected = IllegalAssignment.class)
    public void checkBoolDeclarationSetToDecimal() {
        checkCode("bool b = 1.1;");
    }

    private void checkCode(String code) {
        Start node = null;
        try {
            node = getParser(code).parse();
        } catch (ParserException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (LexerException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        // Apply typechecker
        node.apply(new TypeChecker());
    }

    private Parser getParser(String code) {
        return new Parser(new Lexer(new PushbackReader(new StringReader(code), 1024)));
    }
}
