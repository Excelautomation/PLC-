package dk.aau.sw402F15;

import dk.aau.sw402F15.CodeGenerator.ASTSimplify;
import dk.aau.sw402F15.CodeGenerator.CodeGenerator;
import dk.aau.sw402F15.Exception.CompilerException;
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
/*        String code = "struct car func() {" +
                "struct car bmw;" +
                "return bmw;" +
                "}" +
                "struct car{\n" +
                "int nrOfWheels;\n" +
                "bool isTruck;\n" +
                "}\n" +
                "int main(){\n" +
                "struct car bmw;\n" +
                "func().nrOfWheels = 4;" +
                "return 1;\n" +
                "}\n";*/
            String code = "float main() {" +
                    "float a = 1.4; " +
                    //"a = 1.10 + 1;" +
                    "return a;" +
                    "}";

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

            // Simplifying the AST for easier codegen
            tree.apply(new ASTSimplify());

            // Apply preprocessor
            Preprocessor preprocessor = new Preprocessor();
            tree.apply(preprocessor);

            // Apply scopechecker
            ScopeChecker checker = new ScopeChecker(preprocessor.getScope());
            tree.apply(checker);

            // Applying typechecker
            TypeChecker typeChecker = new TypeChecker(checker.getSymbolTable());
            tree.apply(typeChecker);

            // Apply codegenerator
            tree.apply(new CodeGenerator(typeChecker.getScope()));

        } catch (ParserException e) {
            e.printStackTrace();
        } catch (LexerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CompilerException e) {
            e.printError();
            e.printStackTrace();
        }
    }
}