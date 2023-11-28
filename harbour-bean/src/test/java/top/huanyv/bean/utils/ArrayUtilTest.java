package top.huanyv.bean.utils;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class ArrayUtilTest {

    @Test
    public void map() {

        String[] strings = new String[]{"123", "aaaaa", "a", "00"};

        Integer[] map = ArrayUtil.map(strings, Integer.class, s -> s.length());
        System.out.println("map = " + Arrays.toString(map));

        System.out.println(Arrays.toString(Arrays.copyOfRange(strings, 0, strings.length - 1)));
    }
}