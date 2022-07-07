package com.demo.dynamicdb.way1.fengbao.service.impl;

import com.demo.dynamicdb.way1.fengbao.entity.Student;
import com.demo.dynamicdb.way1.fengbao.mapper.ds1.StudentMapper;
import com.demo.dynamicdb.way1.fengbao.mapper.ds2.TeacherMapper;
import com.demo.dynamicdb.way1.fengbao.service.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl implements IStudentService {
    @Autowired
    private StudentMapper studentMapper;

    @Override
    public Student getById(String id) {
        Student student= studentMapper.getById(id);
        return student;
    }
}
