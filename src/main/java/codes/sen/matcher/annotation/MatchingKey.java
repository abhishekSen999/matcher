package codes.sen.matcher.annotation;

import java.lang.annotation.*;

import static codes.sen.matcher.Constants.DEFAULT_PAIR_ID;

@Retention(RetentionPolicy.RUNTIME)
@Repeatable(MatchingKeys.class)
@Target(ElementType.FIELD)
public @interface MatchingKey {

    int order() default 0;

    String pairId() default DEFAULT_PAIR_ID;

}
