package codes.sen.matcher;

import java.util.Collection;
import java.util.List;

public interface Matcher<L,R> {

    public Collection<Matched<L,R>> match(List<L> leftObjects , List<R> rightObjects, MatcherSpecifications specifications);


}
