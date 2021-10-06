package codes.sen.matcher.invocationHandlers;

import codes.sen.utility.Assert;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Objects;

@Slf4j
public class EqualsAndHashCodeOverrideForMatching<T> implements InvocationHandler {

    private T target;

    private String[] accessorOfFieldsToConsiderForThisTarget;

    private String[] accessorOfFieldsToConsiderForThat; // with which this has to be matched

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
    public static final Object[] fetchFieldsWithAccessors(@NotNull Object obj, @NotNull String... fieldAccessors) {

        Class<?> classOfObj = getClassOfObject(obj);

        Object[] fields_ = new Object[fieldAccessors.length];

        Object targetObject = getTargetObject(obj);

        for (int i = 0; i < fieldAccessors.length; i++) {
            String accessor = fieldAccessors[i];
            Assert.notNull(accessor, "Field Accessor cannot be null");

            try {
                Method accessorMethod = classOfObj.getMethod(accessor);
                accessorMethod.setAccessible(true);
                fields_[i] = accessorMethod.invoke(targetObject);
            } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
                log.error("{} occurred while invoking accessor-{} on object-{}", e.getClass(), accessor, obj);
                e.printStackTrace();
                throw new RuntimeException(e);
            }

        }

        return fields_;

    }

    //todo documentation
    public EqualsAndHashCodeOverrideForMatching(@NotNull T target_
            , @NotNull String[] accessorOfFieldsToConsiderForThisTarget_
            , @NotNull String[] accessorOfFieldsToConsiderForThat_) {

        this.target = target_;
        this.accessorOfFieldsToConsiderForThisTarget = accessorOfFieldsToConsiderForThisTarget_;
        this.accessorOfFieldsToConsiderForThat = accessorOfFieldsToConsiderForThat_;

        this.fieldsToConsiderForThisTarget = fetchFieldsWithAccessors(target, accessorOfFieldsToConsiderForThisTarget);
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
        Object[] fieldsOfObj =fetchFieldsWithAccessors(obj,accessorOfFieldsToConsiderForThisTarget);
        return Arrays.equals(fieldsToConsiderForThisTarget,fieldsOfObj);
    }

    private boolean equalsOverrideForDifferentType(Object obj){
        Object[] fieldsOfObj ;
        try{
            fieldsOfObj = fetchFieldsWithAccessors(obj , accessorOfFieldsToConsiderForThat);
        }catch (Exception e){
            return false;
        }
        return Arrays.equals(fieldsToConsiderForThisTarget,fieldsOfObj);
    }

    public interface ProxiedDataHolder<T> {
        public T getProxiedTarget();

        public Class<? extends T> getClassOfTarget();
    }
}
