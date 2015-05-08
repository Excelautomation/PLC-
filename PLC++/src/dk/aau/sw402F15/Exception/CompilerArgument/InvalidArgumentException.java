package dk.aau.sw402F15.Exception.CompilerArgument;

import dk.aau.sw402F15.Exception.CompilerMessageException;

/**
 * Created by sahb on 08/05/15.
 */
public class InvalidArgumentException extends CompilerMessageException {

    public InvalidArgumentException(String argument) {
        super("Invalid argument: " + argument);
    }
}
