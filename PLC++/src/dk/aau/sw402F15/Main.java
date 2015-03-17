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
                                "bool aaa = 5 + 3 > 7;\n" +
                                "a = 10; int i(){}\n" +
                                "int a = 1 + 5 - 6 * 7;\n" +
                                "bool a = true && false;\n" +
                                "bool b = false || true;\n" +
                                "bool c = false;\n" +
                                "bool d = false == false;\n" +
                                "bool e = true == false;\n" +
                                "bool f = true != false;\n" +
                                "bool g = true != true;\n" +
                                "if (true) { bool b = true; }\n" +
                                "if (false) { bool b = false; b(); bool a = I#0.0; } else { bool c; bool d = d; }\n" +
                                "if (false) { bool c = false; int i = i(); } else if (false) { bool c = true; } else {}\n" +
                                "void b() { bool b = false; b(); return; }\n" +
                                "bool b(int c, int j) { bool b = false; port p = AQ#0.1; return true; }\n" +
                                "b++; ++i; --i; ++i;\n" +
                                "a+=3; b-=2; c*=4; d/=1;\n" +
                                "b(); void b() { } void c() { } port a = I#i; port p = Q#I#1;\n" +
                                "int b = z++; int c = ++z; z += 3; int z = 3;\n" +
                                "if (i > 0) { } else if (i < 0) { } else { }\n" +
                                "int j = i(10);";// +
                                //"bool b = !true; bool b = !(true) && !true;";

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

