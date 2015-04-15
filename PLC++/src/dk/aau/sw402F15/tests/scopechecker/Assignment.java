package dk.aau.sw402F15.tests.scopechecker;

import dk.aau.sw402F15.TypeChecker.Exceptions.SymbolNotFoundException;
import dk.aau.sw402F15.tests.ScopeCheckerTest;
import org.junit.Test;

/**
 * Created by sahb on 15/04/15.
 */
public class Assignment extends ScopeCheckerTest {
    @Test(expected = SymbolNotFoundException.class)
    public void errorOnNonDeclaredVariable() {
        checkCode("int func() {" +
                "i = 1;" +
                "}");
    }

    @Test
    public void canAssignToDeclaredVariable() {
        checkCode("int func() {" +
                "int i = 0;" +
                "i = 2;" +
                "}");
    }

    @Test
    public void canAssignToDeclaredVariableInParentScope() {
        checkCode("int i;" +
                "int func() {" +
                "i = 3;" +
                "}");
    }
}
