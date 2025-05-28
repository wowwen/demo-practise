package com.demo.designmodel.dataaccess;

import java.util.List;

/**
 * @author juven
 * @date 2025/5/29 2:18
 * @description 创建数据访问对象接口
 */
public interface StudentDao {
    public List<Student> getAllStudents();
    public Student getStudent(int rollNo);
    public void updateStudent(Student student);
    public void deleteStudent(Student student);
}
