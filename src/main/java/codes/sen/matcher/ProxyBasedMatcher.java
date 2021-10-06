package codes.sen.matcher;

import codes.sen.matcher.invocationHandlers.EqualsAndHashCodeOverrideForMatching;
import codes.sen.matcher.invocationHandlers.EqualsAndHashCodeOverrideForMatching.ProxiedDataHolder;

import java.lang.reflect.Proxy;
import java.util.*;
import java.util.stream.Collectors;

public class ProxyBasedMatcher<L, R> implements Matcher<L, R> {

    Collection<Matched> matched;

    public static final int MAP_SIZE_MULTIPLICATION_FACTOR = 100;

    //todo add documentation
    @Override
    public Collection<Matched<L,R>> match(List<L> leftObjects, List<R> rightObjects, MatcherSpecifications specs) {
        matched = new ArrayList<Matched>(Math.min(leftObjects.size(), rightObjects.size()));


        List<ProxiedDataHolder<L>> proxiedLeftObjects = leftObjects.stream()
                .map(leftObject ->
                        (ProxiedDataHolder<L>) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(),
                                new Class[]{ProxiedDataHolder.class},
                                new EqualsAndHashCodeOverrideForMatching<L>(leftObject
                                        , specs.getFieldAccessorsForLeft()
                                        , specs.getFieldAccessorsForRight()))
                ).collect(Collectors.toList());


        List<ProxiedDataHolder<R>> proxiedRightObjects = rightObjects.stream()
                .map(rightObject ->
                        (ProxiedDataHolder<R>) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(),
                                new Class[]{ProxiedDataHolder.class},
                                new EqualsAndHashCodeOverrideForMatching<R>(rightObject
                                        , specs.getFieldAccessorsForRight()
                                        , specs.getFieldAccessorsForLeft()))
                ).collect(Collectors.toList());


        // Matching using hashing as equals and hashCode are proxied

        Map<ProxiedDataHolder, Matched<L,R>> map = new HashMap<>(matched.size() * MAP_SIZE_MULTIPLICATION_FACTOR);

        proxiedLeftObjects.stream().forEach(proxiedLeftObject -> {

            if ( ! map.containsKey(proxiedLeftObject))
                map.put(proxiedLeftObject, Matched.getNewMatched(specs));


            map.get(proxiedLeftObject).add(proxiedLeftObject.getProxiedTarget());

        });

        proxiedRightObjects.stream().forEach(proxiedRightObject -> {

            if ( ! map.containsKey(proxiedRightObject))
                map.put(proxiedRightObject, Matched.getNewMatched(specs));


            map.get(proxiedRightObject).add(proxiedRightObject.getProxiedTarget());

        });

        return map.values();

    }

}
