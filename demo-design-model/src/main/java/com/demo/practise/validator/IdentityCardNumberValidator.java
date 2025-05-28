package com.demo.practise.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 *
 * @FileName: IdentityCardNumberValidator
 * @Author: owen
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
