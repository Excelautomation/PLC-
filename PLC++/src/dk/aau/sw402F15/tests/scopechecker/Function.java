package dk.aau.sw402F15.tests.scopechecker;

import dk.aau.sw402F15.Exception.SymbolTable.SymbolNotFoundException;
import dk.aau.sw402F15.tests.ScopeCheckerTest;
import org.junit.Test;

/**
 * Created by sahb on 15/04/15.
 */
public class Function extends ScopeCheckerTest {

    @Test
    public void checkCanCallFunction() {
        checkCode("int func() {" +
                "int i = func();" +
                "}");
    }

    @Test(expected = SymbolNotFoundException.class)
    public void canNotCallNonExcistingFunction() {
        checkCode("int func() {" +
                "int i = func2();" +
                "}");
    }

    @Test
    public void canCallFunctionBelow() {
        checkCode("int func() { int i = func2(); }" +
                  "int func2() { int i = func(); }");
    }
}
