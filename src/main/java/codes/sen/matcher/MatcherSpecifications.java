package codes.sen.matcher;

/**
 * An Interface to provide specifications that are required for Matching objects of two different classes.<br>
 * Take note order of FieldAccessors are important.<p>
 * Example :<br>
 * {@code ClassA{
 *     fieldX;
 *     fieldY;
 *     fieldZ;
 * }}
 *  <br>
 * {@code ClassB{
 *      fieldP;
 *      fieldQ;
 *      fieldR;
 *  }}
 *  are required to be matched on the equality of the following fields
 *
 *  <pre>
 *      | ClassA | ClassB |
 *      |--------|--------|
 *      | fieldX | fieldP |
 *      | fieldY | fieldQ |
 *  </pre>
 *  That is:<br>
 *  ClassA - fieldX with ClassB - fieldP <br>
 *  ClassA - fieldY with ClassB - fieldQ <br>
 *  <p>
 *  Here , say left is ClassA and right is ClassB <br>
 *  then <br>
 *  accessorsForLeft = { accessorOfFieldX , accessorOfFieldY } <br>
 *  accessorsOfRight = { accessorOfFieldP , accessorOfFieldQ }
 * @param <L> left class
 * @param <R> right class
 * @see Matcher
 * @see Matched
 */
public interface MatcherSpecifications<L,R> {

    public Class<L> getClassOfLeftType();

    public String[] getFieldAccessorsForLeft();

    public Class<R> getClassOfRightType();

    public String[] getFieldAccessorsForRight();

}
