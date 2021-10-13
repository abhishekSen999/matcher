package codes.sen.matcher;


import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

import codes.sen.utility.Assert;

//todo documentation
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


    //todo documentation
    public boolean add(Object o) {

        if (this.leftType.isAssignableFrom(o.getClass())) {
            leftObjects.add((L) o);
        } else if (this.rightType.isAssignableFrom(o.getClass()))
            rightObjects.add((R) o);
        else
            return false;

        return true;
    }


    //todo documentation
    public List<L> getLeftObjects(){
        return leftObjects;
    }

    //todo documentation
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

    //todo documentation
    public boolean hasBothLeftAndRight(){
        return ! (leftObjects.isEmpty() || rightObjects.isEmpty());
    }

    //todo documentation
    public static final <L,R> Matched<L,R> getNewMatched(MatcherSpecifications<L , R> specs){
        return new Matched<L,R>(specs.getClassOfLeftType() , specs.getClassOfRightType());
    }
}
