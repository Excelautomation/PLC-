package dk.aau.sw402F15.Exception.FunctionChecker;

import dk.aau.sw402F15.Exception.CompilerMessageException;

public class InitMethodNotExcistsException extends CompilerMessageException {
    public InitMethodNotExcistsException() {
        super("Program needs to have a init method");
    }
}

