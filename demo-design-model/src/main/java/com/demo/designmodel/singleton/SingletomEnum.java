package com.demo.designmodel.singleton;

/**
 * @author juven
 * @date 2025/5/25 16:17
 * @description
 */
public enum SingletomEnum {
    INSTANCE;

    // 可以添加其他成员变量和方法
    private int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    // 可以添加其他业务方法
    public void doSomething() {
        System.out.println("Doing something...");
    }
}

class Main {
    public static void main(String[] args) {
        SingletomEnum singleton = SingletomEnum.INSTANCE;
        singleton.setValue(42);
        System.out.println(singleton.getValue()); // 输出: 42

        // 验证单例
        SingletomEnum anotherInstance = SingletomEnum.INSTANCE;
        System.out.println(singleton == anotherInstance); // 输出: true
    }
}

