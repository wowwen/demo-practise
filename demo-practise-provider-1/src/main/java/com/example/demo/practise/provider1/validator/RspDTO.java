package com.example.demo.practise.provider1.validator;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.Data;
import org.apache.http.HttpStatus;

import java.io.Serializable;

/**
 * ©Copyright ©1968-2020 Midea Group,IT
 *
 * @FileName: RspDTO
 * @Author: jiangyw8
 * @Date: 2020-9-22 17:19
 * @Description: 统一返回结构体
 */
@Data
public class RspDTO<T> implements Serializable {

    private static final long serialVersionUID = -4351174736796141800L;

    private static final int PARAM_FAIL_CODE = 1;
    private static final int PARAM_SUCCESS_CODE = 0;

    private T data;
    private Integer code;
    private String msg;

    public RspDTO(){

    }

    public RspDTO(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public RspDTO(Integer code, String msg, T data) {
        this.data = data;
        this.code = code;
        this.msg = msg;
    }

    public static RspDTO fail(){
        return new RspDTO(PARAM_FAIL_CODE, "系统繁忙,请稍后再试");
    }

    public static RspDTO fail(String msg){
        return new RspDTO(PARAM_FAIL_CODE, msg);
    }

    public static RspDTO error() {
        return new RspDTO(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统繁忙,请稍后再试");
    }

    public static RspDTO<Object> success(Object data){
        return new RspDTO(PARAM_SUCCESS_CODE, "success", data);
    }

    @Override
    public String toString() {
        return JSON.toJSONStringWithDateFormat(this, "yyyy-MM-dd HH:mm:ss", new SerializerFeature[0]);
    }
}
