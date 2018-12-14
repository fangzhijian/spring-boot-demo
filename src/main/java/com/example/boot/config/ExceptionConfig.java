package com.example.boot.config;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import javax.servlet.ServletException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;

/**
 * User: 走路呼呼带风
 * Date: 2018/11/27
 * Time: 22:51
 */
@ControllerAdvice(basePackages = "com.example.boot")
@Slf4j
public class ExceptionConfig {


    //实体类入参验证异常处理
    //@Validated写在方法内括号内
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

    //非实体类入参验证
    //@Validated写在类上面,即和@RestController、@Controller一起
    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseBody
    public String constraintViolationException(ConstraintViolationException constraintViolationException){
        StringBuilder sb = new StringBuilder();
        Set<ConstraintViolation<?>> constraintViolations = constraintViolationException.getConstraintViolations();
        int start = 0;
        for (ConstraintViolation<?> constraintViolation:constraintViolations){
            ConstraintViolationImpl<?> constraintViolation1mpl = (ConstraintViolationImpl<?>) constraintViolation;
            PathImpl pathImpl = (PathImpl) constraintViolation1mpl.getPropertyPath();
            sb.append(pathImpl.getLeafNode().getName()).append(constraintViolation1mpl.getMessage());
            if (start != constraintViolations.size()-1){
                sb.append("、");
            }
            start++;
        }
        return sb.toString();
    }

    //使用@RequestParam对入参做空检验,
    // 一般用于文件和数组空校验,但校验不了isEmpty状态
    @ExceptionHandler(value = {MissingServletRequestParameterException.class, MissingServletRequestPartException.class})
    @ResponseBody
    public String missingParameterException(ServletException servletException){
        String errorMsg = "入参不能为空";
        if (servletException instanceof MissingServletRequestParameterException){
            MissingServletRequestParameterException missingParameterException = (MissingServletRequestParameterException) servletException;
            errorMsg = String.format("%s不能为空",missingParameterException.getParameterName());
        } else if (servletException instanceof MissingServletRequestPartException){
            MissingServletRequestPartException missingPartException = (MissingServletRequestPartException) servletException;
            errorMsg =  String.format("%s文件不能为空",missingPartException.getRequestPartName());
        }
        return errorMsg;
    }

    //@Validated入参验证异常检验
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public String exception(Exception e){
        log.error(e.getMessage(),e);
        return e.getMessage();
    }
}
