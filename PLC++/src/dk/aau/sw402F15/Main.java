package dk.aau.sw402F15;

import dk.aau.sw402F15.Exception.CompilerException;
import dk.aau.sw402F15.parser.lexer.LexerException;
import dk.aau.sw402F15.parser.parser.ParserException;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

public class Main {

    public static void main(String[] args) {
            String code = "void init() {; }" +
                    "" +
                    "void run() {" +
                    "int i = 3 + 4;" +
                    " }"
                    ;

        try {
            Reader reader;
            if(args.length != 0)
                reader = new FileReader(args[0]);
            else
                reader = new StringReader(code);

            Compiler compiler = new Compiler();
            compiler.compile(reader);
        } catch (ParserException e) {
            e.printStackTrace();
        } catch (LexerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CompilerException e) {
            e.printError(code);
        }
    }
}