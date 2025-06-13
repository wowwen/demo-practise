package com.demo.multidatasource.aop.service;

import com.demo.multidatasource.aop.entity.Teacher;

import java.util.List;

public interface ITeacherService {

    List<Teacher> getAll();

    void addTeacher(Teacher teacher) throws Exception;
}
