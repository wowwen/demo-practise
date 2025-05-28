package com.demo.designmodel.businessdelegate;

/**
 * @author juven
 * @date 2025/5/28 23:02
 * @description
 */
public interface BusinessService {
    public void doProcessing();
}

class EJBService implements BusinessService {

    @Override
    public void doProcessing() {
        System.out.println("Processing task by invoking EJB Service");
    }
}

class JMSService implements BusinessService {

    @Override
    public void doProcessing() {
        System.out.println("Processing task by invoking JMS Service");
    }
}
