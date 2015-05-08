package dk.aau.sw402F15;

import dk.aau.sw402F15.CodeGenerator.ASTSimplify;
import dk.aau.sw402F15.CodeGenerator.CodeGenerator;
import dk.aau.sw402F15.Exception.CompilerException;
import dk.aau.sw402F15.Exception.RuntimeCompilerException;
import dk.aau.sw402F15.FunctionChecker.FunctionChecker;
import dk.aau.sw402F15.Preprocessor.Preprocessor;
import dk.aau.sw402F15.ScopeChecker.ScopeChecker;
import dk.aau.sw402F15.TypeChecker.TypeChecker;
import dk.aau.sw402F15.parser.lexer.Lexer;
import dk.aau.sw402F15.parser.lexer.LexerException;
import dk.aau.sw402F15.parser.node.Start;
import dk.aau.sw402F15.parser.parser.Parser;
import dk.aau.sw402F15.parser.parser.ParserException;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.io.StringReader;

/**
 * Created by sahb on 08/05/15.
 */
public class Compiler {
    public void compile(String input) throws ParserException, IOException, LexerException, CompilerException {
        compile(new StringReader(input));
    }

    public void compile(Reader reader) throws ParserException, IOException, LexerException, CompilerException {
        compile(reader, true);
    }

    private void compile(Reader reader, boolean prettyPrint) throws ParserException, IOException, LexerException, CompilerException {
        try {
            // Parse tree
            Parser parser = new Parser(new Lexer(new PushbackReader(reader, 1024)));
            Start tree = parser.parse();

            // Simplifying the AST for easier codegen
            tree.apply(new ASTSimplify());

            // Print tree
            if (prettyPrint) {
                tree.apply(new PrettyPrinter());
            }

            // Apply preprocessor
            Preprocessor preprocessor = new Preprocessor();
            tree.apply(preprocessor);

            // Check if run and init function exists
            FunctionChecker.checkFunctions(preprocessor.getScope());

            // Apply scopechecker
            ScopeChecker checker = new ScopeChecker(preprocessor.getScope());
            tree.apply(checker);

            // Applying typechecker
            TypeChecker typeChecker = new TypeChecker(checker.getSymbolTable());
            tree.apply(typeChecker);

            // Print tree
            tree.apply(new PrettyPrinter());

            // Apply codegenerator
            tree.apply(new CodeGenerator(typeChecker.getScope()));
        } catch (RuntimeCompilerException e) {
            throw new CompilerException(e);
        }
    }
}
