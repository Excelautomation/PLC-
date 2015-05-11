package dk.aau.sw402F15;

import dk.aau.sw402F15.CodeGenerator.ASTSimplify;
import dk.aau.sw402F15.CodeGenerator.CodeGenerator;
import dk.aau.sw402F15.Exception.CompilerArgument.InvalidArgumentException;
import dk.aau.sw402F15.Exception.CompilerArgument.MissingArgumentException;
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

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by sahb on 08/05/15.
 */
public class Compiler {
    public void compile(String args[]) {
        // Init compilerArgs
        CompilerArgs compilerArgs;
        try {
            compilerArgs = new CompilerArgs(args);
        }
        catch (RuntimeCompilerException e) {
            e.printError("");
            return;
        }

        try {
            compile(compilerArgs);
        } catch (FileNotFoundException e) {
            System.err.println("Error: File was not found");
        } catch (ParserException e) {
            e.printStackTrace();
        } catch (LexerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CompilerException e) {
            // Flush stdout
            System.out.flush();

            if (compilerArgs.verbose()) e.printStackTrace();
            try {
                e.printError(getCode(compilerArgs));
            } catch (FileNotFoundException e1) {
                // Should not happen because we read file in compile method - this method is only occuring when an error in the compile method has occured.
                e1.printStackTrace();
            }
        }
    }

    private void compile(CompilerArgs compilerArgs) throws IOException, LexerException, CompilerException, ParserException {
        compile(getReader(compilerArgs), compilerArgs);
    }

    private void compile(Reader reader, CompilerArgs compilerArgs) throws LexerException, CompilerException, ParserException, IOException {
        compile(reader, compilerArgs.prettyPrint(), compilerArgs.verbose());
    }

    private void compile(Reader reader, boolean prettyPrint, boolean verbose) throws ParserException, IOException, LexerException, CompilerException {
        try {
            // Save starttime
            long startTime = System.nanoTime();

            // Parse tree
            if (verbose) System.out.println("Parsing code");
            Parser parser = new Parser(new Lexer(new PushbackReader(reader, 1024)));
            Start tree = parser.parse();

            // Simplifying the AST for easier codegen
            if (verbose) System.out.println("Simplifying AST");
            tree.apply(new ASTSimplify());

            // Apply preprocessor
            if (verbose) System.out.println("Running preprocessor");
            Preprocessor preprocessor = new Preprocessor();
            tree.apply(preprocessor);

            // Check if run and init function exists
            if (verbose) System.out.println("Checking init and run functions");
            FunctionChecker.checkFunctions(preprocessor.getScope());

            // Apply scopechecker
            if (verbose) System.out.println("Running scopechecker");
            ScopeChecker checker = new ScopeChecker(preprocessor.getScope());
            tree.apply(checker);

            // Applying typechecker
            if (verbose) System.out.println("Running typechecker");
            TypeChecker typeChecker = new TypeChecker(checker.getSymbolTable());
            tree.apply(typeChecker);

            // Print tree
            if (prettyPrint) {
                if (verbose) System.out.println("Running prettyprinter");
                tree.apply(new PrettyPrinter());
            }

            // Apply codegenerator
            if (verbose) System.out.println("Running codegenerator");
            tree.apply(new CodeGenerator(typeChecker.getScope()));

            // Print output for total time taken
            long endTime = System.nanoTime() - startTime;
            System.out.println("Compilation done");
            System.out.print("Took: ");

            if (endTime / (1000 * 1000) < 1000) {
                System.out.println(((System.nanoTime() - startTime) / (1000 * 1000)) + "ms");
            } else {
                System.out.println(((System.nanoTime() - startTime) / (1000 * 1000 * 1000)) + "s");
            }
        } catch (RuntimeCompilerException e) {
            throw new CompilerException(e);
        }
    }

    private String getCode(CompilerArgs compilerArgs) throws FileNotFoundException {
        String file = "";
        for (String f : compilerArgs.file()) {
            FileReader fr = new FileReader(f);
            Scanner scanner = new Scanner(fr);
            while (scanner.hasNext())
            {
                file += scanner.nextLine() + "\n";
            }
        }
        return file;
    }

    private Reader getReader(CompilerArgs compilerArgs) throws FileNotFoundException {
        String file = getCode(compilerArgs);
        if (compilerArgs.verbose()) System.out.println(file);
        return new StringReader(file);
    }

    private class CompilerArgs {
        private boolean mPrettyPrint = false;
        private boolean mVerbose;
        private boolean mStd;
        private List<String> mFiles;

        public CompilerArgs(String[] args) {
            this.mFiles = new ArrayList<String>();

            for (int i = 0; i < args.length; i++) {
                if (args[i].equals("--pretty")) {
                    this.mPrettyPrint = true;
                } else if (args[i].equals("--verbose")) {
                    this.mVerbose = true;
                } else if (args[i].equals("--std")) {
                    this.mStd = true;
                } else if (args[i].equals("-file")) {
                    this.mFiles.add(args[++i]);
                } else {
                    // Check if file exists
                    File file = new File(args[i]);
                    if (file.exists() && !file.isDirectory()) {
                        mFiles.add(args[i]);
                    } else {
                        throw new InvalidArgumentException(args[i]);
                    }
                }
            }

            if (this.mFiles.size() == 0) {
                throw new MissingArgumentException("File is missing");
            }

            // Add stdlib
            if (std()) {
                this.mFiles.add(0,"stdlib.ppp");
            }
        }

        public boolean prettyPrint() {
            return mPrettyPrint;
        }

        public boolean verbose() {
            return mVerbose;
        }

        public boolean std() { return mStd; }

        public List<String> file() {
            return mFiles;
        }
    }
}
