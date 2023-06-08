package com.demo.encode.param.entity;

import com.demo.encode.param.annotation.DataMask;
import com.demo.encode.param.enums.DataMaskingEnum;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class User implements Serializable {
    private static final long serialVersionUID = -3250767924498054307L;

    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 姓名
     */
    @DataMask(maskFunc = DataMaskingEnum.ALL_MASK)
    private String name;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 邮箱
     */
    @DataMask(maskFunc = DataMaskingEnum.ALL_MASK)
    private String email;

}
