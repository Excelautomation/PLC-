package dk.aau.sw402F15.Exception.FunctionChecker;

import dk.aau.sw402F15.Exception.CompilerMessageException;

public class RunIsNotAFunctionException extends CompilerMessageException {
    public RunIsNotAFunctionException() {
        super("Run is not a method");
    }
}
