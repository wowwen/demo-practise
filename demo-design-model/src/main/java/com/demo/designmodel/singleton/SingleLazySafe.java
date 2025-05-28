package com.demo.designmodel.singleton;

/**
 * @author juven
 * @date 2025/5/25 1:54
 * @description
 * 这种方式具备很好的 lazy loading，能够在多线程中很好的工作，但是，效率很低，99% 情况下不需要同步
 * 优点：第一次调用才初始化，避免内存浪费
 * 缺点：必须加锁 synchronized 才能保证单例，但加锁会影响效率。getInstance() 的性能对应用程序不是很关键（该方法使用不太频繁）
 */
public class SingleLazySafe {
    public static void main(String[] args) {
        //获取唯一可用的对象
        SingletonSafe object = SingletonSafe.getInstance();
    }
}

class SingletonSafe {
    private static SingletonSafe instance;
    private SingletonSafe (){}
    public static synchronized SingletonSafe getInstance() {
        if (instance == null) {
            instance = new SingletonSafe();
        }
        return instance;
    }
}
