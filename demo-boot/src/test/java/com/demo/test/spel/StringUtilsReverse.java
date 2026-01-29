package com.demo.test.spel;

/**
 * @author jiangyw
 * @date 2026/1/26 14:26
 * @description
 */
public abstract class StringUtilsReverse {

    public static String reverseString(String input) {
        StringBuilder backwards = new StringBuilder();
        for (int i = 0; i < input.length(); i++)
        backwards.append(input.charAt(input.length() - 1 - i));
        return backwards.toString();
    }
}

