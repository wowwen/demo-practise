package com.demo.designmodel.Interceptingfilter;

/**
 * @author juven
 * @date 2025/5/29 2:53
 * @description
 */
public class Client {
    FilterManager filterManager;

    public void setFilterManager(FilterManager filterManager){
        this.filterManager = filterManager;
    }

    public void sendRequest(String request){
        filterManager.filterRequest(request);
    }
}
