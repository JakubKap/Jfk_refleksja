import sample.callable.ICompareOps;

public class MyCompareOps implements ICompareOps {
    @Override
    public boolean ge(int a, int b) {
        if(a>=b)
            return true;
        else
            return false;
    }

    @Override
    public boolean le(int a, int b) {
        if(a<=b)
            return true;
        else
            return false;
    }
}
