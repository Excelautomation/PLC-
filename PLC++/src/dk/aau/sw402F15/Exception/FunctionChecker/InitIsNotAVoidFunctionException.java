package dk.aau.sw402F15.Exception.FunctionChecker;

import dk.aau.sw402F15.Exception.CompilerMessageException;

public class InitIsNotAVoidFunctionException extends CompilerMessageException {
    public InitIsNotAVoidFunctionException() {
        super("Init is not a void function");
    }
}


