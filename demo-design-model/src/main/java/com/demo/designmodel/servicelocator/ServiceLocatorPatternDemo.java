package com.demo.designmodel.servicelocator;

/**
 * @author juven
 * @date 2025/5/29 3:00
 * @description
 */
public class ServiceLocatorPatternDemo
{
    public static void main(String[] args) {
        Service service = ServiceLocator.getService("Service1");
        service.execute();
        service = ServiceLocator.getService("Service2");
        service.execute();
        service = ServiceLocator.getService("Service1");
        service.execute();
        service = ServiceLocator.getService("Service2");
        service.execute();
    }
}
