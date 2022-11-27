package codes.sen.matcher;

import codes.sen.utility.Assert;

import java.util.Collection;
import java.util.List;

public interface AnnotationBasedMatcher<L,R> extends Matcher<L,R>{

    public Collection<Matched<L,R>> match(List<L> leftObjects , List<R> rightObjects , String pairId);
    public Collection<Matched<L,R>> match(List<L> leftObjects , List<R> rightObjects);

    public static <L,R>  AnnotationBasedMatcher<L, R> newInstance(Class<? extends L> lClass , Class<? extends R> rClass ) {
        Assert.nonAssignableTypes(lClass , rClass);
        return new ProxyBasedMatcherWithAnnotationSupport<L,R>();
    }

    public static AnnotationBasedMatcher newInstance() {
        return new ProxyBasedMatcherWithAnnotationSupport();
    }

}
