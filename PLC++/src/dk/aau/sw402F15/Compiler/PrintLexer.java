package dk.aau.sw402F15.Compiler;

import dk.aau.sw402F15.parser.lexer.IPushbackReader;
import dk.aau.sw402F15.parser.lexer.Lexer;
import dk.aau.sw402F15.parser.lexer.LexerException;

import java.io.IOException;
import java.io.PushbackReader;

/**
 * Created by sahb on 11/05/15.
 * http://www.sable.mcgill.ca/listarchives/sablecc-list/msg00134.html
 */
public class PrintLexer extends Lexer {
    public PrintLexer(@SuppressWarnings("hiding") PushbackReader in) {
        super(in);
    }

    public PrintLexer(@SuppressWarnings("hiding") IPushbackReader in) {
        super(in);
    }

    @Override
    protected void filter() throws LexerException, IOException {
        super.filter();

        System.out.println(token.getClass() +
                ", state : " + state.id() +
                ", text : [" + token.getText() + "]");
    }
}
