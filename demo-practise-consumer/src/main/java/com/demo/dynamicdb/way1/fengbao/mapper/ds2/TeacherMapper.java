package com.demo.dynamicdb.way1.fengbao.mapper.ds2;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.demo.dynamicdb.way1.fengbao.entity.Teacher;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TeacherMapper extends BaseMapper<Teacher> {

    Teacher getById(String id);
}
