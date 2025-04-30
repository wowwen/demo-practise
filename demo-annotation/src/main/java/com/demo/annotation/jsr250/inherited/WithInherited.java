package com.demo.annotation.jsr250.inherited;

import java.lang.annotation.*;

/**
 * @author owen
 * @date 2024/10/23 21:25
 * @description 采用@Inherited修饰的注解（如@WithInherited），当此注解标记在父类上时，子类一会自动继承（注意只对类生效，对子接口继承父接口、子类实现父接口不会生效）
 */
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface WithInherited {

}
