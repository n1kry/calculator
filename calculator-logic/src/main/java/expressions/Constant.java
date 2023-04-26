package expressions;

public class Constant implements MathExpression {
    private final int value;

    public Constant(int value) {
        this.value = value;
    }
    @Override
    public int evaluate() {
        return value;
    }
}
