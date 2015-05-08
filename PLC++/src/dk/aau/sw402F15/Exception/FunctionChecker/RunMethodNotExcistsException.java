package dk.aau.sw402F15.Exception.FunctionChecker;

import dk.aau.sw402F15.Exception.CompilerMessageException;

/**
 * Created by sahb on 08/05/15.
 */
public class RunMethodNotExcistsException extends CompilerMessageException {
    public RunMethodNotExcistsException() {
        super("Program needs to have a run method");
    }
}

