package dk.aau.sw402F15;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        String input = " 3.3!= I#0.3 0.3";

        Scanner scanner = new Scanner(input);
        List<Scanner.Token> tokens = scanner.readAll();
        for (Scanner.Token t : tokens) {
            System.out.print(t.getType().toString() + " -> ");
            System.out.println(t.getText());
        }
    }
}