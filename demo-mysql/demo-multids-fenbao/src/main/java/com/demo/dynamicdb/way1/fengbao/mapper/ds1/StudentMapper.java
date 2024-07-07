package com.demo.dynamicdb.way1.fengbao.mapper.ds1;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.demo.dynamicdb.way1.fengbao.entity.Student;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface StudentMapper extends BaseMapper<Student> {

    Student getById(@Param("id") String id);
}
