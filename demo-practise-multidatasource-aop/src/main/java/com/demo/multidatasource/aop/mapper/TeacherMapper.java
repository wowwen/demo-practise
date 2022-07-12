package com.demo.multidatasource.aop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.demo.multidatasource.aop.annotation.DataSource;
import com.demo.multidatasource.aop.entity.Teacher;
import com.demo.multidatasource.aop.enums.DataSourceEnum;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

//@Repository
@Mapper
public interface TeacherMapper extends BaseMapper<Teacher> {

    List<Teacher> selectAllTeachers();
}
