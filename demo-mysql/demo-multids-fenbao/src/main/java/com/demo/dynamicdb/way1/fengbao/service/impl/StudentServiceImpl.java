package com.demo.dynamicdb.way1.fengbao.service.impl;

import com.demo.dynamicdb.way1.fengbao.entity.Student;
import com.demo.dynamicdb.way1.fengbao.entity.Teacher;
import com.demo.dynamicdb.way1.fengbao.mapper.ds1.StudentMapper;
import com.demo.dynamicdb.way1.fengbao.mapper.ds2.TeacherMapper;
import com.demo.dynamicdb.way1.fengbao.service.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudentServiceImpl implements IStudentService {
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private TeacherMapper teacherMapper;

    @Override
    public Student getById(String id) {
        Student student= studentMapper.getById(id);
        return student;
    }

    @Override
    @Transactional
    public Integer addStudent(Student student) {
        int insert = teacherMapper.insert(Teacher.builder().name("王老师" + student.getName()).addr("一中").build());
        int i = 1/0;
        System.out.println("演示多数据源的事务");
        int insert1 = studentMapper.insert(student);

        return insert + insert1;

        /**
         * 在未引入分布式事务管理之前，上面这样玩，会在teacher表中插入数据，但是student表不会插入数据
         * 原因是Datasource1Configuration类中配置的@Primary标注主数据源是ds1，事务注解@Transactional只对主数据源有效
         * 所以teacher的插入不会回滚
         * 解决此问题，需要引入分布式事务
         * 引入发你不是事务Atomikos后，如果报异常，则都会回滚
         */
    }
}
