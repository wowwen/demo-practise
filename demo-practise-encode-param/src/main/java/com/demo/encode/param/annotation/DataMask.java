package com.demo.encode.param.annotation;

import com.demo.encode.param.enums.DataMaskingEnum;

import java.lang.annotation.*;

/**
 * java.lang.annotation 提供了四种元注解，专门注解其他的注解（在自定义注解的时候，需要使用到元注解）：
 */
@Target({ElementType.FIELD,ElementType.TYPE})
//ElementType.FIELD ：成员变量、对象、属性（包括enum实例）。
// ElementType.TYPE ：用于描述类、接口(包括注解类型) 或enum声明。
@Retention(RetentionPolicy.RUNTIME) // 始终不会丢弃，运行期也保留该注解，因此可以使用反射机制读取该注解的信息。我们自定义的注解通常使用这种方式。
@Documented //指定被标注的注解会包含在javadoc中。
public @interface DataMask {
    DataMaskingEnum maskFunc() default DataMaskingEnum.NO_MASK;
}
