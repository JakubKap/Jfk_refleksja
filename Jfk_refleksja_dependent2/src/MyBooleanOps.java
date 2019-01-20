import sample.callable.IBooleanOps;

public class MyBooleanOps implements IBooleanOps {
    @Override
    public boolean neg(boolean value) {
       return !value;
    }

    @Override
    public boolean or(boolean value1, boolean value2) {
        return (value1 || value2);
    }
}
