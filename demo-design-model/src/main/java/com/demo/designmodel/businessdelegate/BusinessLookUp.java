package com.demo.designmodel.businessdelegate;

/**
 * @author juven
 * @date 2025/5/28 23:03
 * @description
 */
public class BusinessLookUp {
    public BusinessService getBusinessService(String serviceType){
        if(serviceType.equalsIgnoreCase("EJB")){
            return new EJBService();
        }else {
            return new JMSService();
        }
    }
}
