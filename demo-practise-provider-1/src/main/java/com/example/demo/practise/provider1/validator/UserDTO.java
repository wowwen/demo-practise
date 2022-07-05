package com.example.demo.practise.provider1.validator;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * ©Copyright ©1968-2020 Midea Group,IT
 *
 * @FileName: UserDTO
 * @Author: jiangyw8
 * @Date: 2020-9-20 23:43
 * @Description: TODO
 */
@Data
public class UserDTO implements Serializable {

    private static final long serialVersionUID = 7225246936234980572L;

    @NotNull(message = "用户Id不能为空", groups = Update.class)
    private Long userId;

    /**
     * 多个group类，需要用{}括起来
     */
    @NotBlank(message = "用户名不能为空")
    @Length(max = 20, message = "用户名不能超过20个字符", groups = {Create.class, Update.class})
    @Pattern(regexp = "^[\\u4E00-\\u9FA5A-Za-z0-9\\*]*$", message = "用户昵称限制：最多20字符，包含文字、字母和数字")
    private String userName;

    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^[1][3,4,5,6,7,8,9][0-9]{9}$", message = "手机号格式有误", groups = Create.class)
    private String mobile;

    private String sex;

    @NotBlank(message = "联系邮箱不能为空")
    @Email(message = "邮箱格式不对")
    private String email;

    private String password;

    @Future(message = "时间必须是将来时间")
    private Date createTime;

    @NotBlank(message = "身份证号码不能为空")
    @IdentityCardNumber(message = "身份证信息有误，请核对后提交")
    private String idCardNo;

    @Valid
    private ScoresDTO scoresDTO;

    @Valid
    private List<MemberDTO> members;
}
