package com.example.boot.config;

import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * User: 走路呼呼带风
 * Date: 2018/11/27
 * Time: 22:51
 */
@ControllerAdvice
public class ExceptionConfig {

    @ExceptionHandler(value = BindException.class)
    @ResponseBody
    public String bindException(BindException bindException){
        List<ObjectError> allErrors = bindException.getAllErrors();
        StringBuilder sb = new StringBuilder();
        int size = allErrors.size();
        for (int i = 0; i <size ; i++) {
            sb.append(allErrors.get(i).getDefaultMessage());
            if (i != size-1 && size>1){
                sb.append("、");
            }
        }
        return sb.toString();
    }
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public String bindException(Exception e){
        return e.getMessage();
    }
}
