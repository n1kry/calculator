import exceptions.InvalidExpressionException;
import logic.Calculator;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter an expression:");
        String expression = scanner.nextLine();
        try {
            int result = Calculator.evaluate(expression);
            System.out.println("Result: " + result);
        } catch (InvalidExpressionException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
