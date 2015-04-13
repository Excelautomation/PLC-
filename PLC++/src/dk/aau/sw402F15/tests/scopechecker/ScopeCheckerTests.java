package dk.aau.sw402F15.tests.scopechecker;


import dk.aau.sw402F15.ScopeChecker.ScopeChecker;
import dk.aau.sw402F15.TypeChecker.TypeChecker;
import dk.aau.sw402F15.parser.lexer.*;
import dk.aau.sw402F15.parser.node.*;
import dk.aau.sw402F15.parser.parser.*;

import java.io.*;

/**
 * Created by sahb on 13/04/15.
 */
public class ScopeCheckerTests {



    private void checkCode(String code) {
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

        // Apply scopechecker
        ScopeChecker scopeChecker = new ScopeChecker();
        node.apply(scopeChecker);
    }

    private Parser getParser(String code) {
        return new Parser(new Lexer(new PushbackReader(new StringReader(code), 1024)));
    }
}
