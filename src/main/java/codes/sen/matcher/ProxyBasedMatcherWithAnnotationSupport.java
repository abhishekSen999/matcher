package codes.sen.matcher;

import codes.sen.matcher.annotation.MatchingKey;
import codes.sen.matcher.annotation.MatchingKeys;
import codes.sen.utility.Assert;

import java.lang.reflect.Field;
import java.util.*;

import static java.util.AbstractMap.SimpleEntry;

class ProxyBasedMatcherWithAnnotationSupport<L,R> extends ProxyBasedMatcher<L,R> implements AnnotationBasedMatcher<L,R> {
    @Override
    public Collection<Matched<L, R>> match(List<L> leftObjects, List<R> rightObjects , String pairId) {

        Assert.notNull(leftObjects,"Object List passed for matching cannot be null");
        Assert.notNull(rightObjects,"Object List passed for matching cannot be null");

        assertNonEmptyList(leftObjects,"Cannot Match empty list , provided LeftSide List is empty." );
        assertNonEmptyList(rightObjects,"Cannot Match empty list , provided RightSide List is empty.");

        L sampleLeftObject = leftObjects.get(0);
        R sampleRightObject = rightObjects.get(0);

        MatcherSpecifications<L,R> matcherSpecifications = createMatcherSpecification(sampleLeftObject , sampleRightObject , pairId);

        return super.match(leftObjects , rightObjects , matcherSpecifications);

    }

    @Override
    public Collection<Matched<L, R>> match(List<L> leftObjects, List<R> rightObjects) {
        return match(leftObjects, rightObjects, Constants.DEFAULT_PAIR_ID);
    }


    private <T> void assertNonEmptyList(List<T> list, String message){
        if(list.isEmpty()){
            throw new IllegalArgumentException(message);
        }
    }

    private MatcherSpecifications<L,R> createMatcherSpecification(L sampleLeftObject , R sampleRightObject , String pairId  ){
        Class<L> leftClass = (Class<L>)sampleLeftObject.getClass();
        Class<R> rightClass = (Class<R>)sampleRightObject.getClass();

        return new MatcherSpecifications<L, R>() {
            @Override
            public Class<L> getClassOfLeftType() {
                return leftClass;
            }

            @Override
            public String[] getFieldsForLeft() {
                return fetchAnnotatedFieldNamesInOrder(getClassOfLeftType(), pairId);
            }

            @Override
            public Class<R> getClassOfRightType() {
                return rightClass;
            }

            @Override
            public String[] getFieldsForRight() {
                return fetchAnnotatedFieldNamesInOrder(getClassOfRightType(), pairId);
            }
        };



    }

    private <T> String[] fetchAnnotatedFieldNamesInOrder(Class<T> class_ , String pairId ){

        Assert.notNull(pairId , "PairId cannot be null while matching.");

        List<SimpleEntry<Field,Integer>> taggedFields = new ArrayList<>();

        for (Field field: class_.getDeclaredFields() ) {
            if(field.isAnnotationPresent(MatchingKeys.class)){ // if @MatchingKey annotation is repeating on the Field

                MatchingKey[] matchingKeys = field.getAnnotation(MatchingKeys.class).value();
                for (MatchingKey key : matchingKeys){
                    if(pairId.equals(key.pairId())){
                        taggedFields.add(new SimpleEntry(field , key.order()));
                    }
                }

            } else if (field.isAnnotationPresent(MatchingKey.class)) { // if @MatchingKey annotation is non-repeating on the field

                MatchingKey key = field.getAnnotation(MatchingKey.class);
                if(pairId.equals(key.pairId())){
                    taggedFields.add(new SimpleEntry(field , key.order()));
                }

            }
        }

        if(taggedFields.isEmpty()){
            throw new IllegalStateException("Matching Cannot be done on Class " + class_.getSimpleName() +
                    " as none of the fields are tagged with @MatchingKey having pairId = \""+pairId+"\"");
        }

        taggedFields.sort(new Comparator<SimpleEntry<Field, Integer>>() {
            @Override
            public int compare(SimpleEntry<Field, Integer> o1, SimpleEntry<Field, Integer> o2) {
                return o1.getValue().compareTo(o2.getValue());
            };

        });

        List<String> orderedFieldNames = new ArrayList<>(taggedFields.size());

        taggedFields.forEach( entry-> orderedFieldNames.add(entry.getKey().getName()) );

        return orderedFieldNames.toArray(new String[orderedFieldNames.size()]);

    }


}
