package com.demo.designmodel.businessdelegate;

/**
 * @author juven
 * @date 2025/5/28 23:04
 * @description
 */
public class Client {

    BusinessDelegate businessService;

    public Client(BusinessDelegate businessService){
        this.businessService  = businessService;
    }

    public void doTask(){
        businessService.doTask();
    }
}
