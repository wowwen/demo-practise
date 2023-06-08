package com.demo.practise.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * ©Copyright ©1968-2020 Midea Group,IT
 *
 * @FileName: IdentityCardNumberValidator
 * @Author: jiangyw8
 * @Date: 2020-9-25 14:28
 * @Description: TODO
 */
public class IdentityCardNumberValidator implements ConstraintValidator<IdentityCardNumber, Object> {
    @Override
    public void initialize(IdentityCardNumber identityCardNumber) {

    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext constraintValidatorContext) {
        return IdCardValidatorUtils.isValidate18Idcard(object.toString());
    }
}
