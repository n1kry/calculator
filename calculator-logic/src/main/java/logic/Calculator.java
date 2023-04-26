package logic;

import exceptions.InvalidExpressionException;
import expressions.Constant;
import expressions.MathExpression;
import expressions.Operation;

import java.util.EmptyStackException;
import java.util.Stack;

public class Calculator {
    public static MathExpression parseExpression(String expression) throws InvalidExpressionException {
        char[] tokens = ExpressionFormatter.render(expression);

        boolean negativePermission = true;

        Stack<MathExpression> values = new Stack<>();
        Stack<Character> ops = new Stack<>();

        try {
            for (int i = 0; i < tokens.length; i++) {
                if (tokens[i] >= '0' && tokens[i] <= '9' || negativePermission && tokens[i] == '-') {
                    StringBuilder sb = new StringBuilder();

                    if (tokens[i] == '-') {
                        sb.append(tokens[i++]);
                    }

                    while (i < tokens.length && tokens[i] >= '0' && tokens[i] <= '9') {
                        sb.append(tokens[i++]);
                    }

                    values.push(new Constant(Integer.parseInt(sb.toString())));
                    negativePermission = false;
                    i--;

                } else if (tokens[i] == '(') {
                    ops.push(tokens[i]);

                } else if (tokens[i] == ')') {
                    while (!ops.empty() && ops.peek() != '(') {
                        char op = ops.pop();
                        MathExpression right = values.pop();
                        MathExpression left = values.pop();
                        values.push(new Operation(op, left, right));
                    }

                    if (!ops.empty()) {
                        ops.pop();
                    }

                } else if (tokens[i] == '+' || tokens[i] == '-' || tokens[i] == '*' || tokens[i] == '/') {
                    while (!ops.empty() && hasPrecedence(tokens[i], ops.peek())) {
                        char op = ops.pop();
                        MathExpression right = values.pop();
                        MathExpression left = values.pop();
                        values.push(new Operation(op, left, right));
                    }

                    ops.push(tokens[i]);
                    negativePermission = true;
                }
            }

            while (!ops.empty()) {
                char op = ops.pop();
                MathExpression right = values.pop();
                MathExpression left = values.pop();
                values.push(new Operation(op, left, right));
            }

        } catch (EmptyStackException | NumberFormatException e) {
            throw new InvalidExpressionException("Invalid expression", e);
        }

        if (values.size() != 1) {
            throw new InvalidExpressionException("Invalid expression");
        }

        return values.pop();
    }

    public static int evaluate(String expression) throws InvalidExpressionException {
        MathExpression mathExpression = parseExpression(expression);
        return mathExpression.evaluate();
    }

    public static int applyOp(char op, int a, int b) throws InvalidExpressionException {
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
                    throw new InvalidExpressionException("Cannot divide by zero");
                return a / b;
            }
        }
        return 0;
    }

    private static boolean hasPrecedence(char op1, char op2) {
        if (op2 == '(' || op2 == ')') {
            return false;
        } else if ((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-')) {
            return false;
        } else {
            return true;
        }
    }
}
