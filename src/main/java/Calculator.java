import javax.xml.transform.TransformerException;
import java.util.EmptyStackException;
import java.util.Scanner;
import java.util.Stack;

public class Calculator {
    public static int evaluate(String expression) {
        char[] tokens = expression.toCharArray();

        boolean negtivePermision = true;

        int closeBracket = 0;
        int openBracket = 0;
        // Stack for operands
        Stack<Integer> values = new Stack<Integer>();

        // Stack for Operators
        Stack<Character> ops = new Stack<Character>();

        try {
            for (int i = 0; i < tokens.length; i++) {
                // Skip whitespace
                if (tokens[i] == ' ') {
                    continue;
                }

                // Push operands onto the stack
                if (tokens[i] >= '0' && tokens[i] <= '9' || negtivePermision && tokens[i] == '-') {
                    StringBuilder sb = new StringBuilder();
                    if (tokens[i] == '-') {
                        sb.append(tokens[i++]);
                    }
                    while (i < tokens.length  && tokens[i] >= '0' && tokens[i] <= '9') {
                        sb.append(tokens[i++]);
                    }
                    values.push(Integer.parseInt(sb.toString()));
                    negtivePermision = false;
                    i--;
                }

                // Handle opening parentheses
                else if (tokens[i] == '(') {
                    ops.push(tokens[i]);
                    openBracket++;
                }

                //else----
                // Handle closing parentheses
                if (tokens[i] == ')') {
                    closeBracket++;
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
                    negtivePermision = true;
                }

                if (i == tokens.length - 1 && openBracket - closeBracket != 0)
                    tokens = addBracket(tokens, openBracket - closeBracket);
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

    public static char[] addBracket(char[] initArr, int size) {
        char[] newArr = new char[initArr.length + size];
        System.arraycopy(initArr, 0, newArr, 0, initArr.length);
        for (int i = initArr.length; i < initArr.length + size; i++) {
            newArr[i] = ')';
        }
        return newArr;
    }

    // Apply the operator to the two operands
    public static int applyOp(char op, int b, int a) {
        switch (op) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0)
                    throw new UnsupportedOperationException("Cannot divide by zero");
                return a / b;
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