package com.demo.designmodel.singleton;

/**
 * @author juven
 * @date 2025/5/25 3:05
 * @description 这种方式采用双锁机制，安全且在多线程情况下能保持高性能getInstance()的性能对应用程序很关键；
 */
public class DoubleCheckLock {
}

class SingletonDCL {
    private volatile static SingletonDCL singleton;
    private SingletonDCL (){}
    public static SingletonDCL getSingleton() {
        if (singleton == null) {
            synchronized (SingletonDCL.class) {
                if (singleton == null) {
                    singleton = new SingletonDCL();
                }
            }
        }
        return singleton;
    }
}
