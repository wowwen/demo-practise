package com.demo.encode.param.interf;

public interface DataMaskingOperation {

    String MASK_CHAR = "*";

    String mask(String content, String maskChar);
}
