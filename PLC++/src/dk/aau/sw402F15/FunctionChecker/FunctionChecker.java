package dk.aau.sw402F15.FunctionChecker;

import dk.aau.sw402F15.Exception.RuntimeCompilerException;
import dk.aau.sw402F15.Exception.FunctionChecker.*;
import dk.aau.sw402F15.Symboltable.Scope;
import dk.aau.sw402F15.Symboltable.Symbol;
import dk.aau.sw402F15.Symboltable.SymbolFunction;
import dk.aau.sw402F15.Symboltable.Type.SymbolType;

/**
 * Created by sahb on 08/05/15.
 */
public class FunctionChecker {
    public static void checkFunctions(Scope scope) throws RuntimeCompilerException {
        // Check if run and init function exists
        Symbol init = scope.getSymbolByName("init");
        Symbol run = scope.getSymbolByName("run");

        // Check if functions exists
        if (init == null) {
            throw new InitMethodNotExcistsException();
        }
        if (run == null) {
            throw new RunMethodNotExcistsException();
        }

        // Check if it's a function
        if (!init.getType().equals(SymbolType.Type.Function)) {
            throw new InitIsNotAFunctionException();
        }
        if (!run.getType().equals(SymbolType.Type.Function)) {
            throw new RunIsNotAFunctionException();
        }

        // Check if returntype is void
        SymbolFunction initFunction = (SymbolFunction)init;
        SymbolFunction runFunction = (SymbolFunction)run;
        if (!initFunction.getReturnType().equals(SymbolType.Type.Void)) {
            throw new InitIsNotAVoidFunctionException();
        }
        if (!runFunction.getReturnType().equals(SymbolType.Type.Void)) {
            throw new RunIsNotAVoidFunctionException();
        }
    }
}
