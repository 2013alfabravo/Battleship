package battleship.view;

import java.util.Scanner;

public class UI {
    private final Scanner scanner = new Scanner(System.in);
    public void print(String string) {
        System.out.println(string);
    }

    public String readInput(String prompt, String format, String error) {
        String input = null;
        boolean done = false;
        while (!done) {
            System.out.print(prompt);
            input = scanner.nextLine();
            System.out.println();
            done = isValidInput(input, format);
            if (!done) {
                System.out.println(error);
                prompt = " \n";
            }
        }
        return input;
    }

    private boolean isValidInput(String input, String format) {
        return input.matches(format);
    }
}
