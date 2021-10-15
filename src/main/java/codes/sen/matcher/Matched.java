package codes.sen.matcher;


import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

import codes.sen.utility.Assert;

/**
 * A Collection to hold objects of two nonAssignable {@code Classes}.
 * That is both {@link Class#isAssignableFrom(Class) L.isAssignableFrom(R)} and {@link Class#isAssignableFrom(Class) R.isAssignableFrom(L)}
 * should return false.
 * @param <L> leftType
 * @param <R> rightType
 */
public class Matched<L, R> {

    private List<L> leftObjects;
    private Class<? extends L> leftType;

    private List<R> rightObjects;
    private Class<? extends R> rightType;


    /**
     * @param leftObjectType  All Left Objects should be of {@code Class<? extends leftObjectType>}
     * @param rightObjectType All Right Objects should be of {@code Class<? extends rightObjectType>}
     */
    public Matched(@NotNull Class<L> leftObjectType, @NotNull Class<R> rightObjectType) {

        Assert.nonAssignableTypes(leftObjectType,rightObjectType);

        this.leftType = leftObjectType;
        this.rightType = rightObjectType;
        leftObjects = new ArrayList<L>();
        rightObjects = new ArrayList<R>();
    }


    /**
     * Add a new {@link Object} to the matched collection. Addition will be successful if the object actually
     * belongs to {@code LeftObjectType} or {@code RightObjectType} specified during instantiation of {@link Matched}.
     * If addition is possible based on above condition , it will get added to the designated {@link List} based on its type.
     * @param o {@link Object} to be added.
     * @return {@code true} if object addition is successful.
     */
    public boolean add(Object o) {

        if (this.leftType.isAssignableFrom(o.getClass())) {
            leftObjects.add((L) o);
        } else if (this.rightType.isAssignableFrom(o.getClass()))
            rightObjects.add((R) o);
        else
            return false;

        return true;
    }


    /**
     *
     * @return the {@link List} of {@code LeftType} objects.
     */
    public List<L> getLeftObjects(){
        return leftObjects;
    }

    /**
     *
     * @return the {@link List} of {@code RightType} objects.
     */
    public List<R> getRightObjects(){
        return rightObjects;
    }

    @Override
    public String toString() {
        return "Matched{" +
                "leftObjects=" + leftObjects +
                ", rightObjects=" + rightObjects +
                '}';
    }

    /**
     *
     * @return {@code true} if {@code this} instance of {@link Matched} has objects of both types.
     */
    public boolean hasBothLeftAndRight(){
        return ! (leftObjects.isEmpty() || rightObjects.isEmpty());
    }

    /**
     * Factory  method to create a new instance of {@link Matched} from {@link MatcherSpecifications}
     * @param specs {@link MatcherSpecifications} to be used to create the instance of {@link Matched}
     * @param <L> leftType
     * @param <R> rightType
     * @return a new instance of {@link Matched}
     */
    public static final <L,R> Matched<L,R> getNewMatched(MatcherSpecifications<L , R> specs){
        return new Matched<L,R>(specs.getClassOfLeftType() , specs.getClassOfRightType());
    }

}
