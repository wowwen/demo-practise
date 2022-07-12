package com.demo.practise.provider1.comparator;

import java.util.Arrays;

/**
 * 演示比较器
 *
 */

public class C1 {
    public static void main(String[] args) {

        int[] ints = {50, 1, 4, 8, 3};
        String[] strings = {"q","a","c"};

        Arrays.sort(ints);

        for (int i = 0; i < ints.length; i++) {
            System.out.print(ints[i] + " ");
        }
        System.out.println();
        Arrays.sort(strings);
        for (String string : strings) {
            System.out.println(string);
        }
    }
}
