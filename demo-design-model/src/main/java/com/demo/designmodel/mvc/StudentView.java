package com.demo.designmodel.mvc;

/**
 * @author juven
 * @date 2025/5/28 22:46
 * @description
 */
public class StudentView {
    public void printStudentDetails(String studentName, String studentRollNo){
        System.out.println("Student: ");
        System.out.println("Name: " + studentName);
        System.out.println("Roll No: " + studentRollNo);
    }
}
