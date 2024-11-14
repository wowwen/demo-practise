package com.demo.datastructure.test.array;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

/**
 * @author jiangyw
 * @date 2024/11/6 14:06
 * @description
 */
@SpringBootTest
class ArrayTest {

    @Test
    void arrayTest() {
        //-------------------------创建数组--------------------------
        //创建方式1：数据类型 []  数组名称 = new 数据类型[数组长度];
        Integer[] integers = new Integer[3];
        //创建方式2：数据类型 [] 数组名称 = {数组元素1，数组元素2，......}
        String[] strArray = {"1", "2", "3"};

        //----------------------访问数组----------------------------
        //声明数组,声明一个长度为3，只能存放int类型的数据
        int[] myArray = new int[3];
        //给myArray第一个元素赋值1
        myArray[0] = 1;
        //访问myArray的第一个元素
        System.out.println(myArray[0]);

        //--------------------遍历数组---------------------------
        //声明数组2,声明一个数组元素为 1,2,3的int类型数组
        int[] myArray2 = {1, 2, 3};
        for (int value : myArray2) {
            System.out.println(value);
        }
    }

}
