package dk.aau.sw402F15.Exception.FunctionChecker;

import dk.aau.sw402F15.Exception.CompilerMessageException;

public class RunIsNotAVoidFunctionException extends CompilerMessageException {
    public RunIsNotAVoidFunctionException() {
        super("Run is not a void function");
    }
}
