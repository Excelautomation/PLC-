package dk.aau.sw402F15;

import dk.aau.sw402F15.Preprocessor.Preprocessor;
import dk.aau.sw402F15.ScopeChecker.ScopeChecker;
import dk.aau.sw402F15.TypeChecker.TypeChecker;
import dk.aau.sw402F15.parser.lexer.Lexer;
import dk.aau.sw402F15.parser.lexer.LexerException;
import dk.aau.sw402F15.parser.node.Start;
import dk.aau.sw402F15.parser.parser.Parser;
import dk.aau.sw402F15.parser.parser.ParserException;

import java.io.*;

public class Main {

    public static void main(String[] args) {
        String code = "struct car func() {" +
                "struct car bmw;" +
                "return bmw;" +
                "}" +
                "struct car{\n" +
                "int nrOfWheels;\n" +
                "bool isTruck;\n" +
                "}\n" +
                "int main(){\n" +
                "struct car bmw;\n" +
                "func().nrOfWheels = 4;\n" +
                "}\n";

        //System.out.println(code);

        try {
            Reader reader;
            if(args.length != 0)
                reader = new FileReader(args[0]);
            else
                reader = new StringReader(code);
            Parser parser = new Parser(new Lexer(new PushbackReader(reader, 1024)));
            Start tree = parser.parse();

            // Print tree
            tree.apply(new PrettyPrinter());

            // Apply preprocessor
            Preprocessor preprocessor = new Preprocessor();
            tree.apply(preprocessor);

            ScopeChecker checker = new ScopeChecker(preprocessor.getScope());
            tree.apply(checker);

            tree.apply(new TypeChecker(checker.getSymbolTable()));

            tree.apply(new ASTSimplify());

        } catch (ParserException e) {
            e.printStackTrace();
        } catch (LexerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}