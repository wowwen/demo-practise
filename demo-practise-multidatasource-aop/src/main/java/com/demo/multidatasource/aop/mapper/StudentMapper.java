package com.demo.multidatasource.aop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.demo.multidatasource.aop.entity.Student;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

//1.@ComponentScan注解扫描包
//
//@SpringBootApplication中集成了@ComponentScan注解 , 默认扫描Application同级包及子级包中的Bean , 但是会自动过滤调接口 , 所有并不介意在Dao层接口使用 @Repository注解 , 因为即便使用了 , 也不会扫到;
//
//如果你指定扫描dao层包 @ComponentScan(basePackages = {“com.boot.dao”}) 那么会导致Controller层注解失效;
//————————————————
//版权声明：本文为CSDN博主「刘强i」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
//原文链接：https://blog.csdn.net/Sx_Ll_Qiang/article/details/114646597

//Spring在扫描注解时，取消了扫描抽象类和接口，所以无法找到你用@reponsitory注解的dao接口。
// 如果在idea中使用@Mapper注解，在@Autowired时，idea会提示找不到bean，但是不影响运行。
// 但是我看着又难受，百度一堆规避的方法，包括设置改为warning等，后来发现一个骚操作，可以把两个注解同时使用，
// 这样，@Mapper可以让你找到bean，@reponsitory可以帮你治疗强迫症。
//@Repository
//@Repository是Spring的注解，使用在接口上，会报
//Field studentMapper in com.example.demo.practise.provider2.others.service.StudentService required a bean of type 'com.example.demo.practise.provider2.others.mapper.StudentMapper' that could not be found.

@Mapper
public interface StudentMapper extends BaseMapper<Student> {

    List<Student> selectAllStudents();
}
