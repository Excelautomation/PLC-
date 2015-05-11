package dk.aau.sw402F15;

public class Main {

    public static void main(String[] args) {
        Compiler compiler = new Compiler();
        if (args.length != 0)
            compiler.compile(args);
        else
            compiler.compile(new String[] { "code.ppp" });
    }
}