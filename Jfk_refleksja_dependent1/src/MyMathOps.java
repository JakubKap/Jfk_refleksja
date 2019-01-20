import sample.callable.IMathOps;

public class MyMathOps implements IMathOps {
    @Override
    public int round(double value) {
        return (int) Math.round(value);
    }

    @Override
    public int sum(int a, int b) {
        return a + b;
    }
}
