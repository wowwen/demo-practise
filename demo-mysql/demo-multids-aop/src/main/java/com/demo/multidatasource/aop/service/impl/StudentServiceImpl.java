package com.demo.multidatasource.aop.service.impl;

import com.demo.multidatasource.aop.annotation.DataSource;
import com.demo.multidatasource.aop.entity.Student;
import com.demo.multidatasource.aop.enums.DataSourceEnum;
import com.demo.multidatasource.aop.mapper.StudentMapper;
import com.demo.multidatasource.aop.service.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author jans9
 */
@Service
public class StudentServiceImpl implements IStudentService {
    @Resource
    private StudentMapper studentMapper;

    @Override
    @DataSource(DataSourceEnum.PRIMARY)
    public List<Student> getAll() {
        return studentMapper.selectAllStudents();
    }

    @Override
    @DataSource(DataSourceEnum.PRIMARY)
    @Transactional(rollbackFor = Exception.class)
    public void addStudent(Student student) throws Exception {
        final int i = studentMapper.insert(student);
        throw new Exception("模拟异常");
    }
}
