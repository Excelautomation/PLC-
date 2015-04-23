package dk.aau.sw402F15.tests;


import dk.aau.sw402F15.Preprocessor.Preprocessor;
import dk.aau.sw402F15.ScopeChecker.ScopeChecker;
import dk.aau.sw402F15.Symboltable.Scope;
import dk.aau.sw402F15.parser.lexer.Lexer;
import dk.aau.sw402F15.parser.lexer.LexerException;
import dk.aau.sw402F15.parser.node.Node;
import dk.aau.sw402F15.parser.node.Start;
import dk.aau.sw402F15.parser.parser.Parser;
import dk.aau.sw402F15.parser.parser.ParserException;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.StringReader;

/**
 * Created by sahb on 13/04/15.
 */
public abstract class ScopeCheckerTest {
    protected void checkCode(String code) {
        Start node = null;
        try {
            node = getParser(code).parse();
        } catch (ParserException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (LexerException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        // Apply preprocessor
        Preprocessor preprocessor = new Preprocessor();
        node.apply(preprocessor);

        // Apply scopechecker
        ScopeChecker scopeChecker = new ScopeChecker(preprocessor.getScope());
        node.apply(scopeChecker);
    }

    protected Parser getParser(String code) {
        return new Parser(new Lexer(new PushbackReader(new StringReader(code), 1024)));
    }
}

