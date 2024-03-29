package com.demo.practise.validator;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 *
 * @FileName: member
 * @Author: jiangyw8
 * @Date: 2020-9-27 11:30
 * @Description: TODO
 */
@Data
public class MemberDTO  implements Serializable {

    private static final long serialVersionUID = -5091109419414307604L;

    @NotBlank(message = "成员名称不能为空")
    private String name;
}
