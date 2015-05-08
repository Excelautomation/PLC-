package dk.aau.sw402F15.Exception.FunctionChecker;

import dk.aau.sw402F15.Exception.CompilerMessageException;

public class InitIsNotAFunctionException extends CompilerMessageException {
    public InitIsNotAFunctionException() {
        super("Init is not a method");
    }
}

