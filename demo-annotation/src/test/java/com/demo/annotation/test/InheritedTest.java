package com.demo.annotation.test;

import com.demo.annotation.jsr250.inherited.ASonExtendAFatherWithInherited;
import com.demo.annotation.jsr250.inherited.BSonExtendsBFatherClassNoInherited;
import com.demo.annotation.jsr250.inherited.CSonImplementsCFatherInterface;
import com.demo.annotation.jsr250.inherited.DSonInterfaceExtendsDFatherInterfaceWithInherited;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.annotation.Annotation;
import java.util.Arrays;

/**
 * @author jiangyw
 * @date 2024/10/24 0:23
 * @description
 */
@SpringBootTest
class InheritedTest {

    @Test
    void sonExtendFatherInheritedTest(){
        //获取自身和父类的注解
        Annotation[] annotations = ASonExtendAFatherWithInherited.class.getAnnotations();
        for (Annotation annotation : annotations) {
            System.out.println("AAAA注解名：" + annotation); //打印出注解名：@com.demo.annotation.jsr250.inherited.WithInherited()
            //结论：子类继承了父类的注解
        }
    }

    @Test
    void sonExtendsFatherNoInheritedTest(){
        Class<BSonExtendsBFatherClassNoInherited> bSonClazz =
                BSonExtendsBFatherClassNoInherited.class;
        Annotation[] annotations = bSonClazz.getAnnotations();
        for (Annotation annotation : annotations) {
            System.out.println("BBBBB注解名：" + annotation); //打印出注解名：@com.demo.annotation.selfdefine.datacheck.CheckUserData(checkUserData=false)
                                                            //可以看出没有打印出父类的注解@NoInherited，因为@NoInherited没有用@Inherited注解标注
            //结论：子类没有继承父类未标注@Inherited的注解
        }
        //以下是反射的拓展，与注解示例无关
        //----------------自身和父类的公有字段--------------
        System.out.println("自身和父类的公有字段" + Arrays.toString(bSonClazz.getFields()));
        //---------------自身所有字段-------------------
        System.out.println("自身所有字段" + Arrays.toString(bSonClazz.getDeclaredFields()));
        //--------------自身和父类的共有方法----------------
        System.out.println("自身和父类的共有方法" + Arrays.toString(bSonClazz.getMethods()));
        //--------------自身所有方法---------------------
        System.out.println("自身所有方法" + Arrays.toString(bSonClazz.getDeclaredMethods()));
        //-------------自身公有的构造方法-----------------
        System.out.println("自身公有的构造方法" + Arrays.toString(bSonClazz.getConstructors()));
        //-------------自身的所有构造方法-----------------
        System.out.println("自身的所有构造方法" + Arrays.toString(bSonClazz.getDeclaredConstructors()));
        //------------获取自身和父类的注解----------------
        System.out.println("获取自身和父类的注解" + Arrays.toString(bSonClazz.getAnnotations()));
        //------------只获取自身的注解------------------
        System.out.println("只获取自身的注解" + Arrays.toString(bSonClazz.getDeclaredAnnotations()));
        /**
         * BBBBB注解名：@com.demo.annotation.selfdefine.datacheck.CheckUserData(checkUserData=false)
         * 自身和父类的公有字段[]
         * 自身所有字段[]
         * 自身和父类的共有方法[
         * public final native void java.lang.Object.wait(long) throws java.lang.InterruptedException,
         * public final void java.lang.Object.wait(long,int) throws java.lang.InterruptedException,
         * public final void java.lang.Object.wait() throws java.lang.InterruptedException, public boolean java.lang
         * .Object.equals(java.lang.Object),
         * public java.lang.String java.lang.Object.toString(),
         * public native int java.lang.Object.hashCode(),
         * public final native java.lang.Class java.lang.Object.getClass(),
         * public final native void java.lang.Object.notify(),
         * public final native void java.lang.Object.notifyAll()]
         * 自身所有方法[]
         * 自身公有的构造方法[public com.demo.annotation.jsr250.inherited.BSonExtendsBFatherClassNoInherited()]
         * 自身的所有构造方法[public com.demo.annotation.jsr250.inherited.BSonExtendsBFatherClassNoInherited()]
         * 获取自身和父类的注解[@com.demo.annotation.selfdefine.datacheck.CheckUserData(checkUserData=false)]
         * 只获取自身的注解[@com.demo.annotation.selfdefine.datacheck.CheckUserData(checkUserData=false)]
         */

    }

    @Test
    void sonImplementsFatherInterfaceWithInherited(){
        Annotation[] annotations = CSonImplementsCFatherInterface.class.getAnnotations();
        for (Annotation annotation : annotations) {
            System.out.println("CCC注解名：" + annotation);//没有结果打印
            //结论：子类获取不到父接口中标注了@Inherited的注解，即实现父接口不行
        }
    }

    @Test
    void sonInterfaceExtendsDFatherInterfaceWithInherited(){
        Annotation[] annotations = DSonInterfaceExtendsDFatherInterfaceWithInherited.class.getAnnotations();
        for (Annotation annotation : annotations) {
            System.out.println("DDD注解名：" + annotation);//没有结果打印
            //结论：子接口获取不到父接口中标注了@Inherited的注解，即实现父接口不行
        }
    }
    /**
     * 总结：只有子类继承父类才可以；子类实现父接口，子接口继承父接口都不能传递父接口标注了@Inherited的注解
     */
}
