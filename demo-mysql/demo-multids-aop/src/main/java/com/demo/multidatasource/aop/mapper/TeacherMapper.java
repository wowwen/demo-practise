package com.demo.multidatasource.aop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.demo.multidatasource.aop.entity.Teacher;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

//@Repository
@Mapper
public interface TeacherMapper extends BaseMapper<Teacher> {

//    @Select("select * from teacher")
    List<Teacher> selectAllTeachers();
}
