package com.aacoin.dig;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

public class BaseResource {
    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public <T extends Object> void validator(T entity) throws ResourceException {
//        try {
            Set<ConstraintViolation<T>> validators = validator.validate(entity);
            for (ConstraintViolation<T> constraintViolation : validators) {
                throw new ResourceException(constraintViolation.getMessage());
            }
//        } catch (Exception e) {
//            throw new ResourceException(e.getMessage(), e);
//        }
    }

}


