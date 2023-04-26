package exceptions;

public class InvalidExpressionException extends Exception {
    public InvalidExpressionException(String invalidExpression) {
        super(invalidExpression);
    }

    public InvalidExpressionException(String invalidExpression, RuntimeException e) {
        super(invalidExpression, e);
    }
}
