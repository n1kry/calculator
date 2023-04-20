import java.util.Stack;

public class Expression {
    public static void main(String[] args) {
        String input = "(11 + 18) * 20 - 2";
        System.out.println(evaluateExpression(input)); // Expected output: 578

        input = "(11 + 18 * 20 - 2";
        System.out.println(evaluateExpression(input)); // Expected output: 369

        input = "(11 + 18 * (20 - 2";
        System.out.println(evaluateExpression(input)); // Expected output: 335
    }

    public static int evaluateExpression(String input) {
        Stack<Integer> operandStack = new Stack<>();
        Stack<Character> operatorStack = new Stack<>();
        input = input.replaceAll("\\s", ""); // remove all whitespace

        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);
            if (ch == '(') {
                operatorStack.push(ch);
            } else if (ch == ')') {
                while (operatorStack.peek() != '(') {
                    int result = applyOperator(operandStack.pop(), operandStack.pop(), operatorStack.pop());
                    operandStack.push(result);
                    if (operatorStack.isEmpty()) {
                        throw new IllegalArgumentException("Invalid expression: missing opening parenthesis");
                    }
                }
                operatorStack.pop(); // remove the '(' from the operator stack
            } else if (isOperator(ch)) {
                while (!operatorStack.isEmpty() && hasHigherPrecedence(ch, operatorStack.peek())) {
                    int result = applyOperator(operandStack.pop(), operandStack.pop(), operatorStack.pop());
                    operandStack.push(result);
                }
                operatorStack.push(ch);
            } else if (Character.isDigit(ch)) {
                StringBuilder sb = new StringBuilder();
                sb.append(ch);
                while (i+1 < input.length() && Character.isDigit(input.charAt(i+1))) {
                    sb.append(input.charAt(i+1));
                    i++;
                }
                operandStack.push(Integer.parseInt(sb.toString()));
            } else {
                throw new IllegalArgumentException("Invalid character: " + ch);
            }
        }

        while (!operatorStack.isEmpty()) {
            if (operatorStack.peek() == '(') {
                throw new IllegalArgumentException("Invalid expression: missing closing parenthesis");
            }
            int result = applyOperator(operandStack.pop(), operandStack.pop(), operatorStack.pop());
            operandStack.push(result);
        }

        if (operandStack.size() != 1) {
            throw new IllegalArgumentException("Invalid expression: too many operands");
        }

        return operandStack.pop();
    }

    private static int applyOperator(int operand2, int operand1, char operator) {
        switch (operator) {
            case '+':
                return operand1 + operand2;
            case '-':
                return operand1 - operand2;
            case '*':
                return operand1 * operand2;
            case '/':
                if (operand2 == 0) {
                    throw new IllegalArgumentException("Cannot divide by zero");
                }
                return operand1 / operand2;
            default:
                throw new IllegalArgumentException("Invalid operator: " + operator);
        }
    }

    private static boolean isOperator(char ch) {
        return ch == '+' || ch == '-' || ch == '*' || ch == '/';
    }

    private static boolean hasHigherPrecedence(char op1, char op2) {
        if (op2 == '(' || op2 == ')') {
            return false;
        }
        return (op1 != '*' && op1 != '/') || (op2 != '+' && op2 != '-');
    }
}
