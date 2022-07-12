package com.demo.practise.provider1.comparator;

import java.util.Arrays;

public class ComparatorTest {
    /**
     * 测试重写了compareTo方法的student类
     * @param args
     */
//    public static void main(String[] args) {
//        Student s1 = new Student(19, "张三");
//        Student s2 = new Student(18, "李四");
//
//        int i = s1.compareTo(s2);
//        if (i > 0){
//            System.out.println(s1.getName() + "年龄大");
//        }
//        if (i == 0){
//            System.out.println("s1与s2一样大");
//        }
//        if (i < 0){
//            System.out.println(s2.getName() + "年龄大");
//        }
//    }


    /**
     * Student重写了compareTo后，通过 Arrays.sort进行排序
     * @param args
     */
//    public static void main(String[] args) {
//        Student s1 = new Student(19, "张三");
//        Student s2 = new Student(18, "李四");
//        Student s3 = new Student(17 ,"王五");
//
//        Student[] students = {s1, s2, s3};
//        System.out.println("排序前");
//        for (Student student : students) {
//            System.out.println(student.toString());
//        }
//
//        //进行排序
//        Arrays.sort(students);
//
//        System.out.println("排序后");
//        for (Student student : students) {
//            System.out.println(student.toString());
//        }
//    }

    public static void main(String[] args) {
        User a = new User(18, "a");
        User b = new User(18, "b");
        User c = new User(22, "c");

        User[] users = {a, b, c};
        System.out.println("排序前");
        for (User user : users) {
            System.out.println(user.toString());
        }

        Arrays.sort(users);
        System.out.println("排序后");
        for (User user : users) {
            System.out.println(user.toString());
        }
    }
}
