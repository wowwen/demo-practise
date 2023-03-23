package com.demo.practise.validator;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.Data;

/**
 * ©Copyright ©1968-2020 Midea Group,IT
 *
 * @FileName: BizException
 * @Author: jiangyw8
 * @Date: 2020-9-25 10:26
 * @Description: TODO
 */
@Data
public class BizException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private Integer code;
    private String msg;

    public BizException(Integer code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public BizException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public BizException(String msg, Throwable cause) {
        super(msg, cause);
        this.msg = msg;
    }

    public BizException(Integer code, String msg,Throwable cause) {
        super(msg, cause);
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String toString() {
        return JSON.toJSONStringWithDateFormat(this, "yyyy-MM-dd HH:mm:ss", new SerializerFeature[0]);
    }
}
