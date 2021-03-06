package com.demo.multidatasource.aop.service.impl;

import com.demo.multidatasource.aop.annotation.DataSource;
import com.demo.multidatasource.aop.entity.Teacher;
import com.demo.multidatasource.aop.enums.DataSourceEnum;
import com.demo.multidatasource.aop.mapper.TeacherMapper;
import com.demo.multidatasource.aop.service.ITeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherServiceImpl implements ITeacherService {

    @Autowired
    private TeacherMapper teacherMapper;

    @Override
    @DataSource(DataSourceEnum.DATASOURCE1)
    public List<Teacher> getAll() {
        List<Teacher> teachers = teacherMapper.selectAllTeachers();
        return teachers;
    }
}
