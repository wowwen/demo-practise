package com.demo.encode.param.enums;

import com.demo.encode.param.interf.DataMaskingOperation;
import org.springframework.util.StringUtils;

public enum DataMaskingEnum {
    NO_MASK((str, maskChar) -> {
        return str;
    }),

    ALL_MASK((str, maskChar) -> {
        if (StringUtils.hasLength(str)) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < str.length() ; i++) {
                sb.append(StringUtils.hasLength(maskChar) ? maskChar : DataMaskingOperation.MASK_CHAR);
            }
            return sb.toString();
        }else {
            return str;
        }
    });

    private final DataMaskingOperation operation;

    private DataMaskingEnum(DataMaskingOperation operation) {
        this.operation = operation;
    }

    public DataMaskingOperation getOperation() {
        return this.operation;
    }

}

