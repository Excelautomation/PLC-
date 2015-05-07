package dk.aau.sw402F15.Exception.Preprocessor;

import dk.aau.sw402F15.Exception.CompilerException;

/**
 * Created by sahb on 06/05/15.
 */
public abstract class PreprocessorException extends CompilerException {
    public PreprocessorException() {
    }

    public PreprocessorException(String message) {
        super(message);
    }
}
