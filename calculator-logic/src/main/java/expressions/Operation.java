package expressions;

import exceptions.InvalidExpressionException;
import logic.Calculator;

public class Operation implements MathExpression {
    private final char operator;
    private final MathExpression left;
    private final MathExpression right;

    public Operation(char operator, MathExpression left, MathExpression right) {
        this.operator = operator;
        this.left = left;
        this.right = right;
    }
    @Override
    public int evaluate() {
        int leftValue = left.evaluate();
        int rightValue = right.evaluate();
        try {
            return Calculator.applyOp(operator, leftValue, rightValue);
        } catch (InvalidExpressionException e) {
            throw new RuntimeException(e);
        }
    }
}
