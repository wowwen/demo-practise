package com.demo.designmodel.servicelocator;

/**
 * @author juven
 * @date 2025/5/29 2:56
 * @description
 */
public interface Service
{
    public String getName();
    public void execute();
}

class Service1 implements Service {
    public void execute(){
        System.out.println("Executing Service1");
    }

    @Override
    public String getName() {
        return "Service1";
    }
}

class Service2 implements Service {
    public void execute(){
        System.out.println("Executing Service2");
    }

    @Override
    public String getName() {
        return "Service2";
    }
}
