package com.demo.practise.validator;

import lombok.Data;

import javax.validation.constraints.*;
import java.io.Serializable;

/**
 *
 * @FileName: Scores
 * @Author: owen
 * @Date: 2020-9-27 9:15
 * @Description: TODO
 */
@Data
public class ScoresDTO implements Serializable {

    private static final long serialVersionUID = -7169061373359356949L;

    @DecimalMin(value = "0.0", message = "不能低于0分")
    @DecimalMax(value = "100.5", message = "不能高于100.5分")
    private Double mathScore;

    @Min(value = 0, message = "不能低于0分")
    @Max(value = 100, message = "不能高于100分")
    private Double englishScore;

}
