package codes.sen.matcher;

import codes.sen.utility.Assert;

import java.util.Collection;
import java.util.List;

/**
 * {@code Matcher} is a utility tool to match {@code List} of objects of two different {@code Classes}
 * based on specifications provided in form of {@link MatcherSpecifications}.
 * It can only be used to match objects of classes which are of mutually nonAssignable types.
 * That is both {@link Class#isAssignableFrom(Class) L.isAssignableFrom(R)} and {@link Class#isAssignableFrom(Class) R.isAssignableFrom(L)}
 * should return false.
 * To create a new Instance of {@code Matcher}, use provided factory method {@link Matcher#newInstance(Class, Class) Matcher.newInstance}
 * @param <L>  class of Left Type
 * @param <R> class of Right Type
 * @see MatcherSpecifications
 * @see Matched
 */
public interface Matcher<L,R> {

    /**
     * Matches the provided list of objects . Matching happens based on provided {@link MatcherSpecifications }<br>
     *
     * @param leftObjects the {@link List} of objects of LeftType
     * @param rightObjects the {@link List} of objects of RightType
     * @param specifications {@link MatcherSpecifications} that will be used to match the LeftType objects with RightType Objects
     * @return A {@link Collection} of {@link Matched} were each {@code Matched} contains the two lists, one of matching LeftType objects and other of matching RightType objects
     */
    public Collection<Matched<L,R>> match(List<L> leftObjects , List<R> rightObjects, MatcherSpecifications specifications);


    /**
     * Factory method to create a new instance of {@link Matcher}
     * @param lClass {@code Class} of LeftType
     * @param rClass {@code Class} of RightType
     * @param <L> leftType
     * @param <R> rightType
     * @return a new {@link ProxyBasedMatcher}
     */
    public static <L,R>  Matcher newInstance(Class<? extends L> lClass , Class<? extends R> rClass ) {
        Assert.nonAssignableTypes(lClass , rClass);
        return new ProxyBasedMatcher<L,R>();
    }

}
