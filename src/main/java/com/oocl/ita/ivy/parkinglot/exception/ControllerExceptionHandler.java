package com.oocl.ita.ivy.parkinglot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(UsernameOrPasswordIncorrectException.class)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public Map<String, Object> handleUserOrPasswordIncorrectException(UsernameOrPasswordIncorrectException e) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("message", "Username or password incorrect.");
        return result;
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ExceptionResponseEntity handleBusinessException(BusinessException e) {
        return ExceptionResponseEntity.convert(e);
    }
}
