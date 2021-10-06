package codes.sen.matcher;

public interface MatcherSpecifications<L,R> {

    public Class<L> getClassOfLeftType();

    public String[] getFieldAccessorsForLeft();

    public Class<R> getClassOfRightType();

    public String[] getFieldAccessorsForRight();

}
