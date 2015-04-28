package dk.aau.sw402F15.tests.scopechecker;

import dk.aau.sw402F15.tests.ScopeCheckerTest;
import org.junit.Test;

/**
 * Created by sahb on 28/04/15.
 */
public class Struct extends ScopeCheckerTest {
    @Test
    public void simpleFunctionInStruct() {
        checkCode("" +
                "struct car {                               " +
                "   int motorSize = 3;                      " +
                "                                           " +
                "   void updateMotorSize(int size) {        " +
                "       motorSize = size;                   " +
                "   }                                       " +
                "}                                          ");
    }

    @Test
    public void checkSimpleFunctionCallInStruct() {
        checkCode("" +
                "struct car {                               " +
                "   int motorSize = 3;                      " +
                "                                           " +
                "   void updateMotorSize(int size) {        " +
                "       motorSize = size;                   " +
                "   }                                       " +
                "}                                          " +
                "                                           " +
                "void run() {                               " +
                "   struct car c;                           " +
                "   c.updateMotorSize(10);                  " +
                "}                                          ");
    }

    @Test
    public void checkNestedCallOfFunction() {
        checkCode("" +
                "struct car {                               " +
                "   int motorSize = 3;                      " +
                "                                           " +
                "   void updateMotorSize(int size) {        " +
                "       motorSize = size;                   " +
                "   }                                       " +
                "}                                          " +
                "                                           " +
                "struct car getCar() {                      " +
                "   struct car c;" +
                "   return c;" +
                "}                                          " +
                "                                           " +
                "void run() {                               " +
                "   struct car c = getCar();                " +
                "   c.updateMotorSize(10);                  " +
                "   getCar().updateMotorSize(10);           " +
                "}                                          ");
    }
}
