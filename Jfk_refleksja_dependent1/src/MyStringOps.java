import sample.callable.IStringOps;

public class MyStringOps implements IStringOps{

    @Override
    public String substr(String value, int begin) {
        return value.substring(begin);
    }

    @Override
    public char charAt(String value, int index) {
        return value.charAt(index);
    }
}
