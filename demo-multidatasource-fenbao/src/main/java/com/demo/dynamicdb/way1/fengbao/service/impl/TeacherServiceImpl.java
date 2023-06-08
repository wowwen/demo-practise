package com.demo.dynamicdb.way1.fengbao.service.impl;

import com.demo.dynamicdb.way1.fengbao.entity.Teacher;
import com.demo.dynamicdb.way1.fengbao.mapper.ds2.TeacherMapper;
import com.demo.dynamicdb.way1.fengbao.service.ITeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeacherServiceImpl implements ITeacherService {

    @Autowired
    private TeacherMapper teacherMapper;

    @Override
    public Teacher getById(String id) {
        Teacher teacher = teacherMapper.getById(id);
        return teacher;
    }
}
