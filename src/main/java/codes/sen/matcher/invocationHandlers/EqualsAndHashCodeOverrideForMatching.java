package codes.sen.matcher.invocationHandlers;

import codes.sen.utility.Assert;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;
import java.lang.reflect.*;
import java.util.Arrays;
import java.util.Objects;

/**
 * {@code EqualsAndHashCodeOverrideForMatching} is an InvocationHandler that dynamically overrides {@code equals}
 * and {@code hashCode} functions  and a few other functions to facilitate Matching of objects using hashing.
 * @param <T> Type of object whose {@code equals} and {@code hashCode} is overridden by {@link EqualsAndHashCodeOverrideForMatching}
 * @see codes.sen.matcher.Matcher
 * @see codes.sen.matcher.ProxyBasedMatcher
 */
@Slf4j
public class EqualsAndHashCodeOverrideForMatching<T> implements InvocationHandler {

    private T target;

    private String[] fieldNamesToConsiderForThisTarget;

    private String[] fieldNamesToConsiderForThat; // with which this has to be matched

    Object[] fieldsToConsiderForThisTarget;

    //todo write documentation
    public static final Class<?> getClassOfObject(Object obj) {
        if (obj instanceof Proxy) {
            return ((ProxiedDataHolder) obj).getClassOfTarget();
        } else
            return obj.getClass();
    }

    //todo write documentation
    public static final Object getTargetObject(@NotNull Object obj) {
        if (obj instanceof Proxy) {
            return ((ProxiedDataHolder) obj).getProxiedTarget();
        } else
            return obj;
    }

    //todo documentation
    public static final Object[] fetchFieldsWithFieldName(@NotNull Object obj, @NotNull String... fieldNames) {

        Class<?> classOfObj = getClassOfObject(obj);

        Object[] fields_ = new Object[fieldNames.length];

        Object targetObject = getTargetObject(obj);

        for (int i = 0; i < fieldNames.length; i++) {
            String fieldName = fieldNames[i];
            Assert.notNull(fieldName, "Field Name cannot be null");

            try {
                Field field = classOfObj.getDeclaredField(fieldName);
                field.setAccessible(true);
                fields_[i] = field.get(targetObject);
            } catch ( IllegalAccessException |  NoSuchFieldException e) {
                log.error("{} occurred while invoking fieldName-{} on object-{}", e.getClass(), fieldName, obj);
                e.printStackTrace();
                throw new RuntimeException(e);
            }

        }

        return fields_;

    }

    //todo documentation
    public EqualsAndHashCodeOverrideForMatching(@NotNull T target_
            , @NotNull String[] fieldsToConsiderForThisTarget_
            , @NotNull String[] fieldsToConsiderForThat_) {

        this.target = target_;
        this.fieldNamesToConsiderForThisTarget = fieldsToConsiderForThisTarget_;
        this.fieldNamesToConsiderForThat = fieldsToConsiderForThat_;

        this.fieldsToConsiderForThisTarget = fetchFieldsWithFieldName(target, fieldNamesToConsiderForThisTarget);
    }

    private int hashCodeOverride() {
        return Objects.hash(fieldsToConsiderForThisTarget);
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        String methodName = method.getName();

        switch (methodName){
            case "hashCode" : return hashCodeOverride();
            case "equals" : return equalsOverride(args[0]);
            case "getProxiedTarget" : return target;
            case "getClassOfTarget" : return target != null ? target.getClass() : null;
            default: return method.invoke(target,args);
        }

    }


    private boolean equalsOverride(Object that) {
        if (target == that) return true;
        if (target == null || that == null) return false;

        if(getClassOfObject(target).equals(getClassOfObject(that))){
            return equalsOverrideForSameType(that);
        }else
            return equalsOverrideForDifferentType(that);

    }

    private boolean equalsOverrideForSameType(Object obj){
        Object[] fieldsOfObj = fetchFieldsWithFieldName(obj, fieldNamesToConsiderForThisTarget);
        return Arrays.equals(fieldsToConsiderForThisTarget,fieldsOfObj);
    }

    private boolean equalsOverrideForDifferentType(Object obj){
        Object[] fieldsOfObj ;
        try{
            fieldsOfObj = fetchFieldsWithFieldName(obj , fieldNamesToConsiderForThat);
        }catch (Exception e){
            return false;
        }
        return Arrays.equals(fieldsToConsiderForThisTarget,fieldsOfObj);
    }

    /**
     * {@code ProxiedDataHolder} is an interface to facilitate dynamic overriding of Equals and hashCode function for {@link EqualsAndHashCodeOverrideForMatching}
     * @param <T> Type of object whose {@code equals} and {@code hashCode} is overridden by {@link EqualsAndHashCodeOverrideForMatching}
     */
    public interface ProxiedDataHolder<T> {
        public T getProxiedTarget();

        public Class<? extends T> getClassOfTarget();
    }
}
