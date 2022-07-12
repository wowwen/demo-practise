package com.demo.multidatasource.aop.service.impl;

import com.demo.multidatasource.aop.annotation.DataSource;
import com.demo.multidatasource.aop.entity.Student;
import com.demo.multidatasource.aop.enums.DataSourceEnum;
import com.demo.multidatasource.aop.mapper.StudentMapper;
import com.demo.multidatasource.aop.service.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements IStudentService {
    @Autowired
    private StudentMapper studentMapper;

    @Override
    public List<Student> getAll() {
        return studentMapper.selectAllStudents();
    }
}
