package com.demo.designmodel.strategymodel;

/**
 *
 * @FileName: UserType
 * @Author: jiangyw8
 * @Date: 2020-11-26 16:53
 * @Description: 枚举类
 */
public enum UserType {
    SILVER_VIP(1),
    GOLD_VIP(2),
    PLATINUM_VIP(3);

    UserType(Integer code){
        this.code = code;
    }

    private Integer code;

    public Integer getCode(){
        return code;
    }
    public void setCode(Integer code){
        this.code = code;
    }
}
