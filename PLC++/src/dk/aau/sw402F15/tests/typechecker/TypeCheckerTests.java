package dk.aau.sw402F15.tests.typechecker;

import dk.aau.sw402F15.Preprocessor.Preprocessor;
import dk.aau.sw402F15.ScopeChecker.ScopeChecker;
import dk.aau.sw402F15.Symboltable.Scope;
import dk.aau.sw402F15.TypeChecker.Exceptions.*;
import dk.aau.sw402F15.TypeChecker.Exceptions.IllegalAssignmentException;
import dk.aau.sw402F15.TypeChecker.Exceptions.IllegalComparisonException;
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

    @Test(expected = IllegalAssignmentException.class)
    public void checkIntDeclarationSetToBool() {
        checkCode("int i = true;");
    }

    @Test(expected = IllegalAssignmentException.class)
    public void checkIntDeclarationSetToDecimal() {
        checkCode("int i = 1.1;");
    }

    @Test(expected = IllegalAssignmentException.class)
    public void checkBoolDeclarationSetToInt() {
        checkCode("bool b = 1;");
    }

    @Test(expected = IllegalAssignmentException.class)
    public void checkBoolDeclarationSetToDecimal() {
        checkCode("bool b = 1.1;");
    }

    @Test(expected = IllegalAssignmentException.class)
    public void checkDecimalSetToBool(){
        checkCode("float f = true;");
    }

    @Test(expected = IllegalAssignmentException.class)
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

    @Test(expected = IllegalComparisonException.class)
    public void checkIntDecimalComparisonGreater(){
        checkCode("bool b = 1 > 1.1;");
    }

    @Test(expected = IllegalComparisonException.class)
    public void checkIntDecimalComparisonGreaterOrEqual(){
        checkCode("bool b = 1 >= 1.1;");
    }

    @Test(expected = IllegalComparisonException.class)
    public void checkIntDecimalComparisonLess(){
        checkCode("bool b = 1 < 1.1;");
    }

    @Test(expected = IllegalComparisonException.class)
    public void checkIntDecimalComparisonLessOrEqual(){
        checkCode("bool b = 1 <= 1.1;");
    }

    @Test(expected = IllegalComparisonException.class)
    public void checkIntBoolComparisonGreater(){
        checkCode("bool b = 1 > true;");
    }

    @Test(expected = IllegalComparisonException.class)
    public void checkIntBoolComparisonGreaterOrEqual(){
        checkCode("bool b = 1 >= true;");
    }

    @Test(expected = IllegalComparisonException.class)
    public void checkIntBoolComparisonLess(){
        checkCode("bool b = 1 < true;");
    }

    @Test(expected = IllegalComparisonException.class)
    public void checkIntBoolComparisonLessOrEqual(){
        checkCode("bool b = 1 <= true;");
    }

    @Test(expected = IllegalAssignmentException.class)
    public void checkAssignBoolExprToInt() {
        checkCode("int i = 7 < 8;");
    }

    @Test(expected = IllegalReturnTypeException.class)
    public void checkReturnInVoidFunction() {
        checkCode("void func(){return 2;}");
    }

    @Test
    public void checkNoReturnInVoidFunction() {
        checkCode("void func(){}");
    }

    @Test(expected = MissingReturnStatementException.class)
    public void checkMissingReturnStatement() {
        checkCode("int func(){}");
    }

    @Test
    public void checkNoMissingReturnStatement() {
        checkCode("int func(){return 2;}");
    }

    @Test(expected = WrongParameterException.class)
    public void checkWrongParameterInFunction() {
        checkCode("int func(int k, int p){ func(2.2, 2); return k + p; } ");
    }

    @Test
    public void checkNoWrongParameterInFunction() {
        checkCode("int func(int k, int p){ func(2, 2); return k + p; } ");
    }

    @Test(expected = RedefinitionOfReadOnlyException.class)
    public void checkAssignmentOfConst() { checkCode("const int i = 7; int func() { i = 2; } "); }

    @Test(expected = RedefinitionOfReadOnlyException.class)
    public void checkAssignmentOfConstInFunction() { checkCode("int func(){const int i = 7; i = 2; return 5;} "); }


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

        // Apply preprocessor
        Preprocessor preprocessor = new Preprocessor();
        node.apply(preprocessor);

        // Apply scopechecker
        ScopeChecker scopeChecker = new ScopeChecker(preprocessor.getScope());
        node.apply(scopeChecker);

        // Apply typechecker
        node.apply(new TypeChecker(scopeChecker.getSymbolTable()));
    }

    private Parser getParser(String code) {
        return new Parser(new Lexer(new PushbackReader(new StringReader(code), 1024)));
    }
}
