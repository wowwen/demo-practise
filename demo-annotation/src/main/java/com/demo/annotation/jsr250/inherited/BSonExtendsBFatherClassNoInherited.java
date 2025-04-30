package com.demo.annotation.jsr250.inherited;

import com.demo.annotation.selfdefine.datacheck.CheckUserData;

/**
 * @author owen
 * @date 2024/11/4 18:04
 * @description
 */
@CheckUserData//这里只是随便举例，说明父类的注解没有@Inherited时，子类继承父类，获取子类上的注解的时候，只能获取到子类的，获取不到父类的
public class BSonExtendsBFatherClassNoInherited extends BFatherClassNoInherited {
}
