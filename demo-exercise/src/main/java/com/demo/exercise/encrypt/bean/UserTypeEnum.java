package com.demo.exercise.encrypt.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum UserTypeEnum {
    VIP("VIP用户"),
    COMMON("普通用户");
    private String code;
    private String type;

    UserTypeEnum(String type) {
        this.code = name();
        this.type = type;
    }
}