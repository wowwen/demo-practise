package com.demo.designmodel.singleton;

/**
 * @author juven
 * @date 2025/5/25 1:45
 * @description
 * 这种方式是最基本的实现方式，这种实现最大的问题就是不支持多线程；
 * 因为没有加锁 synchronized，所以严格意义上它并不算单例模式
 * 这种方式 lazy loading 很明显，不要求线程安全，在多线程不能正常工作
 */
public class SingletonLazyUnsafe {
    public static void main(String[] args) {
        //获取唯一可用的对象
        Singleton object = Singleton.getInstance();
    }
}

class Singleton {
    private static Singleton instance;
    private Singleton (){}

    public static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }
}




