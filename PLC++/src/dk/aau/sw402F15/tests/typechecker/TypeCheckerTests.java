package dk.aau.sw402F15.tests.typechecker;

import dk.aau.sw402F15.TypeChecker.Exceptions.IllegalAssignment;
import dk.aau.sw402F15.TypeChecker.Exceptions.IllegalComparison;
import dk.aau.sw402F15.TypeChecker.Symboltable.Scope;
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

    @Test
    public void checkIntDeclaration(){
        checkCode("int i = 0;");
    }

    @Test
    public void checkBoolDeclaration() {
        checkCode("bool b = true;");
    }

    @Test
    public void checkDecimalDeclaration() {
        checkCode("float b = 1.1;");
    }

    @Test(expected = IllegalAssignment.class)
    public void checkIntDeclarationSetToBool() {
        checkCode("int i = true;");
    }

    @Test(expected = IllegalAssignment.class)
    public void checkIntDeclarationSetToDecimal() {
        checkCode("int i = 1.1;");
    }

    @Test(expected = IllegalAssignment.class)
    public void checkBoolDeclarationSetToInt() {
        checkCode("bool b = 1;");
    }

    @Test(expected = IllegalAssignment.class)
    public void checkBoolDeclarationSetToDecimal() {
        checkCode("bool b = 1.1;");
    }

    @Test(expected = IllegalAssignment.class)
    public void checkDecimalSetToBool(){
        checkCode("float f = true;");
    }

    @Test(expected = IllegalAssignment.class)
    public void checkDecimalSetToInt(){
        checkCode("float f = 1;");
    }

    @Test
    public void checkIntIntComparisonGreater(){
        checkCode("bool b = 1 > 2;");
    }

    @Test
    public void checkIntIntComparisonGreaterOrEqual(){
        checkCode("bool b = 1 >= 2;");
    }

    @Test
    public void checkIntIntComparisonLess(){
        checkCode("bool b = 1 < 2;");
    }

    @Test
    public void checkIntIntComparisonLessOrEqual(){
        checkCode("bool b = 1 <= 2;");
    }

    @Test(expected = IllegalComparison.class)
    public void checkIntDecimalComparisonGreater(){
        checkCode("bool b = 1 > 1.1;");
    }

    @Test(expected = IllegalComparison.class)
    public void checkIntDecimalComparisonGreaterOrEqual(){
        checkCode("bool b = 1 >= 1.1;");
    }

    @Test(expected = IllegalComparison.class)
    public void checkIntDecimalComparisonLess(){
        checkCode("bool b = 1 < 1.1;");
    }

    @Test(expected = IllegalComparison.class)
    public void checkIntDecimalComparisonLessOrEqual(){
        checkCode("bool b = 1 <= 1.1;");
    }

    @Test(expected = IllegalComparison.class)
    public void checkIntBoolComparisonGreater(){
        checkCode("bool b = 1 > true;");
    }

    @Test(expected = IllegalComparison.class)
    public void checkIntBoolComparisonGreaterOrEqual(){
        checkCode("bool b = 1 >= true;");
    }

    @Test(expected = IllegalComparison.class)
    public void checkIntBoolComparisonLess(){
        checkCode("bool b = 1 < true;");
    }

    @Test(expected = IllegalComparison.class)
    public void checkIntBoolComparisonLessOrEqual(){
        checkCode("bool b = 1 <= true;");
    }

    @Test(expected = IllegalAssignment.class)
    public void checkAssignBoolExprToInt() {
        checkCode("int i = 7 < 8;");
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
        node.apply(new TypeChecker(new Scope(null, null)));
    }

    private Parser getParser(String code) {
        return new Parser(new Lexer(new PushbackReader(new StringReader(code), 1024)));
    }
}