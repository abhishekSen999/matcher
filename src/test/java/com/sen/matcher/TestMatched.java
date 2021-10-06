package com.sen.matcher;

import codes.sen.matcher.Matched;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestMatched {


    @Test
    public void testMatchedCannotBeCreatedFromAssignableTypes() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Matched(Number.class, Integer.class));
    }

    @Test
    public void testAdd() {

        String[] strings = {"a"  , "b" , "c" , "d" , "e"};
        Integer[] integers = {1,2,3,4,5 , 6};

        Matched<String, Integer> matched = new Matched(String.class, Integer.class);
        Stream.of(new String[]{ ""}).filter(obj->false).collect(Collectors.toList());

        List.of(strings).forEach(matched::add);
        List.of(integers).forEach(matched::add);

        Assertions.assertArrayEquals(strings , matched.getLeftObjects().toArray());
        Assertions.assertArrayEquals(integers,matched.getRightObjects().toArray());

    }


    @Test
    public void testHasBothLeftAndRightWithEmptyMatched(){
        Matched<String, Integer> matched = new Matched(String.class, Integer.class);

        Assertions.assertFalse(matched.hasBothLeftAndRight());

    }


    @Test
    public void testHasBothLeftAndRightWithEmptyLeft(){
        Matched<String, Integer> matched = new Matched(String.class, Integer.class);
        matched.add(1);
        Assertions.assertFalse(matched.hasBothLeftAndRight());

    }

    @Test
    public void testHasBothLeftAndRightWithEmptyRight(){
        Matched<String, Integer> matched = new Matched(String.class, Integer.class);
        matched.add("Hola");
        Assertions.assertFalse(matched.hasBothLeftAndRight());
    }

    @Test
    public void testHasBothLeftAndRightWithBothLeftAndRight(){
        Matched<String, Integer> matched = new Matched(String.class, Integer.class);
        matched.add(1);
        matched.add("Hola");
        Assertions.assertTrue(matched.hasBothLeftAndRight());

    }




}
