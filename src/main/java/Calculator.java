import java.util.EmptyStackException;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calculator {

    public static char[] formattingString(String expression) {
        String formatString = expression.replaceAll("\\s", "");
        formatString = formatString.replaceAll("\\)\\(", ")*(");

        Pattern pattern = Pattern.compile("[-+*/(]-\\(");
        Matcher matcher = pattern.matcher(formatString);

        while (matcher.find()) {
            formatString = formatString.replace(matcher.group(), matcher.group().charAt(0)+"-1*(");
        }
        pattern = Pattern.compile("[0-9]\\(");
        matcher = pattern.matcher(formatString);

        while (matcher.find()) {
            formatString = formatString.replace(matcher.group(), matcher.group().charAt(0)+"*(");
        }

        formatString = formatString.replaceAll("^-\\(", "-1*(");

        long openBrackets = formatString.chars().filter(ch -> ch == '(').count();
        long closeBrackets = formatString.chars().filter(ch -> ch == ')').count();
        long newSize = openBrackets - closeBrackets;

        char[] tokens = new char[(int) (formatString.length() + newSize)];

        System.arraycopy(formatString.toCharArray(),0,tokens,0,formatString.length());

        for (int i = formatString.length(); i < tokens.length; i++) {
            tokens[i] = ')';
        }

        return tokens;
    }
    public static int evaluate(String expression) {

        char[] tokens = formattingString(expression);
        System.out.println(tokens);
        boolean negativePermission = true;

        // Stack for operands
        Stack<Integer> values = new Stack<>();

        // Stack for Operators
        Stack<Character> ops = new Stack<>();

        try {
            for (int i = 0; i < tokens.length; i++) {
                // Push operands onto the stack
                if (tokens[i] >= '0' && tokens[i] <= '9' || negativePermission && tokens[i] == '-') {
                    StringBuilder sb = new StringBuilder();
                    if (tokens[i] == '-') {
                        sb.append(tokens[i++]);
                    }
                    while (i < tokens.length  && tokens[i] >= '0' && tokens[i] <= '9') {
                        sb.append(tokens[i++]);
                    }
                    values.push(Integer.parseInt(sb.toString()));
                    negativePermission = false;
                    i--;
                }

                // Handle opening parentheses
                else if (tokens[i] == '(') {
                    ops.push(tokens[i]);
                }

                // Handle closing parentheses
                else if (tokens[i] == ')') {
                    while (!ops.empty() && ops.peek() != '(') {
                        values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                    }
                    if (!ops.empty()) {
                        ops.pop(); // remove opening parenthesis from stack
                    }
                }

                // Handle operators
                else if (tokens[i] == '+' || tokens[i] == '-' ||
                        tokens[i] == '*' || tokens[i] == '/') {
                    while (!ops.empty() && hasPrecedence(tokens[i], ops.peek())) {
                        values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                    }
                    ops.push(tokens[i]);
                    negativePermission = true;
                }
            }

            // Evaluate remaining expressions
            while (!ops.empty()) {
                    values.push(applyOp(ops.pop(), values.pop(), values.pop()));
            }

        } catch (EmptyStackException e) {
            throw new RuntimeException(new Throwable("Wrong input expression"));
        }
        // Final result is the only element on the stack
        return values.pop();
    }

    // Apply the operator to the two operands
    public static int applyOp(char op, int b, int a) {
        switch (op) {
            case '+' -> {
                return a + b;
            }
            case '-' -> {
                return a - b;
            }
            case '*' -> {
                return a * b;
            }
            case '/' -> {
                if (b == 0)
                    throw new UnsupportedOperationException("Cannot divide by zero");
                return a / b;
            }
        }
        return 0;
    }

    // Check operator precedence
    public static boolean hasPrecedence(char op1, char op2) {
        if (op2 == '(' || op2 == ')')
            return false;
        return (op1 != '*' && op1 != '/') || (op2 != '+' && op2 != '-');
    }

    // Test the code
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String expression = scanner.nextLine();
        //"(11 * (2 / 2 + (15 * (20 - 2";
        System.out.println(evaluate(expression));
    }
}