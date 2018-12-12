package com.example.boot.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
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
@ControllerAdvice(basePackages = "com.example.boot")
@Slf4j
public class ExceptionConfig {

    //入参验证异常处理
    @ExceptionHandler(value = BindException.class)
    @ResponseBody
    public String bindException(BindException bindException){
        List<ObjectError> allErrors = bindException.getAllErrors();
        StringBuilder sb = new StringBuilder();
        int size = allErrors.size();
        for (int i = 0; i <size ; i++) {
            ObjectError objectError = allErrors.get(i);
            if (objectError instanceof FieldError){
                FieldError fieldError = (FieldError) objectError;
                sb.append(fieldError.getField()).append(fieldError.getDefaultMessage());
            }else {
                sb.append(objectError.getDefaultMessage());
            }
            if (i != size-1){
                sb.append("、");
            }
        }
        return sb.toString();
    }

    //系统异常验证处理
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public String exception(Exception e){
        log.error(e.getMessage(),e);
        return e.getMessage();
    }
}
