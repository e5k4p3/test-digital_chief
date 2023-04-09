package com.e5k4p3.digitalchief.utils;

import com.e5k4p3.digitalchief.exceptions.ValidationException;
import lombok.experimental.UtilityClass;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

@UtilityClass
public final class ValidationErrorsHandler {
    public static void logValidationErrors(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            for (ObjectError error : bindingResult.getAllErrors()) {
                throw new ValidationException(error.getDefaultMessage());
            }
        }
    }
}