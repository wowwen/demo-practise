package com.demo.designmodel.transferobject;

/**
 * @author juven
 * @date 2025/5/29 3:22
 * @description  创建传输对象 StudentVO
 */
public class StudentVO
{
    private String name;
    private int rollNo;

    StudentVO(String name, int rollNo){
        this.name = name;
        this.rollNo = rollNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRollNo() {
        return rollNo;
    }

    public void setRollNo(int rollNo) {
        this.rollNo = rollNo;
    }
}
