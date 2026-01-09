package com.demo.dynamicdb.way1.fengbao.service;

import com.demo.dynamicdb.way1.fengbao.entity.Student;

public interface IStudentService {

    Student getById(String id);

    Integer addStudent(Student student);
}
