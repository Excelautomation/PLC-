package dk.aau.sw402F15;

import dk.aau.sw402F15.parser.lexer.Lexer;
import dk.aau.sw402F15.parser.lexer.LexerException;
import dk.aau.sw402F15.parser.node.Start;
import dk.aau.sw402F15.parser.parser.Parser;
import dk.aau.sw402F15.parser.parser.ParserException;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.StringReader;

public class Main {

    public static void main(String[] args) {
	    String code =
                                "bool aaa = 5 > 7;" +
                                "a = 10;" +
                                "int a = 1 + 5 - 6 * 7;" +
                                "bool a = true && false;" +
                                "bool b = false || true;" +
                                "bool c = false;" +
                                "bool d = false == false;" +
                                "bool e = true == false;" +
                                "bool f = true != false;" +
                                "bool g = true != true;";

        System.out.println(code);

        try {
            Parser parser = new Parser(new Lexer(new PushbackReader(new StringReader(code), 1024)));
            Start tree = parser.parse();

            // Print tree
            tree.apply(new ExpressionEvaluator());

        } catch (ParserException e) {
            e.printStackTrace();
        } catch (LexerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}

