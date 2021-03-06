package com.example.boot.config;

import com.example.boot.errorCode.PubError;
import com.example.boot.exception.BusinessException;
import com.example.boot.model.InfoJson;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;

/**
 * User: 走路呼呼带风
 * Date: 2018/11/27
 * Time: 22:51
 */
@RestControllerAdvice(basePackages = "com.example.boot")
@Slf4j
public class ExceptionConfig {


    //实体类入参验证异常处理
    //@Validated写在方法内括号内,@RequestBody发生的异常为MethodArgumentNotValidException
    @ExceptionHandler(value = {BindException.class, MethodArgumentNotValidException.class})
    public InfoJson bindException(Exception exception, HttpServletRequest request) {
        logErrorRequestInfo(request);

        List<ObjectError> allErrors;
        if (exception instanceof BindException) {
            BindException bindException = (BindException) exception;
            allErrors = bindException.getAllErrors();
        } else {
            MethodArgumentNotValidException methodArgumentNotValidException = (MethodArgumentNotValidException) exception;
            allErrors = methodArgumentNotValidException.getBindingResult().getAllErrors();
        }

        StringBuilder sb = new StringBuilder();
        int size = allErrors.size();
        for (int i = 0; i < size; i++) {
            ObjectError objectError = allErrors.get(i);
            if (objectError instanceof FieldError) {
                FieldError fieldError = (FieldError) objectError;
                sb.append(fieldError.getField()).append(fieldError.getDefaultMessage());
            } else {
                sb.append(objectError.getDefaultMessage());
            }
            if (i != size - 1) {
                sb.append("、");
            }
        }
        log.error(sb.toString());
        return InfoJson.setFailed(PubError.P2002_PARAM_ERROR.code(), sb.toString());
    }

    //非实体类入参验证
    //@Validated写在类上面,即和@RestController、@Controller一起
    @ExceptionHandler(value = ConstraintViolationException.class)
    public InfoJson constraintViolationException(ConstraintViolationException constraintViolationException, HttpServletRequest request) {
        logErrorRequestInfo(request);

        StringBuilder sb = new StringBuilder();
        Set<ConstraintViolation<?>> constraintViolations = constraintViolationException.getConstraintViolations();
        int start = 0;
        for (ConstraintViolation<?> constraintViolation : constraintViolations) {
            ConstraintViolationImpl<?> constraintViolation1mpl = (ConstraintViolationImpl<?>) constraintViolation;
            PathImpl pathImpl = (PathImpl) constraintViolation1mpl.getPropertyPath();
            sb.append(pathImpl.getLeafNode().getName()).append(constraintViolation1mpl.getMessage());
            if (start != constraintViolations.size() - 1) {
                sb.append("、");
            }
            start++;
        }
        log.error(sb.toString());
        return InfoJson.setFailed(PubError.P2002_PARAM_ERROR.code(), sb.toString());
    }

    //使用@RequestParam对入参做空检验,
    // 一般用于文件和数组空校验,但校验不了isEmpty状态
    //@RequestBody json反序列化异常会触发HttpMessageNotReadableException
    @ExceptionHandler(value = {MissingServletRequestParameterException.class, MissingServletRequestPartException.class, HttpMessageNotReadableException.class})
    public InfoJson missingParameterException(Exception exception, HttpServletRequest request) {
        logErrorRequestInfo(request);
        String errorMsg = "入参不能为空";
        if (exception instanceof MissingServletRequestParameterException) {
            MissingServletRequestParameterException missingParameterException = (MissingServletRequestParameterException) exception;
            errorMsg = String.format("%s不能为空", missingParameterException.getParameterName());
        } else if (exception instanceof MissingServletRequestPartException) {
            MissingServletRequestPartException missingPartException = (MissingServletRequestPartException) exception;
            errorMsg = String.format("%s文件不能为空", missingPartException.getRequestPartName());
        } else if (exception instanceof HttpMessageNotReadableException) {
            if (exception.getMessage().startsWith("Required request body is missing")) {
                errorMsg = "入参json不能为空";
            } else {
                errorMsg = "入参json结构不正确";
            }
        }
        log.error(errorMsg);
        return InfoJson.setFailed(PubError.P2002_PARAM_ERROR.code(), errorMsg);
    }

    //捕捉自定义异常
    @ExceptionHandler(value = BusinessException.class)
    public InfoJson businessException(BusinessException e, HttpServletRequest request) {
        logErrorRequestInfo(request);
        log.error(e.getMessage());
        return InfoJson.setFailed(e.getCode(), e.getMessage());
    }

    //系统异常验证处理
    @ExceptionHandler(value = Exception.class)
    public InfoJson exception(Exception e, HttpServletRequest request) {
        logErrorRequestInfo(request);
        log.error(e.getMessage(), e);
        return InfoJson.setFailed(PubError.P2003_SYSTEM_EXCEPTION.code(), PubError.P2003_SYSTEM_EXCEPTION.message());
    }

    //打印错误的请求体信息
    private void logErrorRequestInfo(HttpServletRequest request) {
        log.error("异常链接:{}", request.getRequestURL());
        Enumeration<String> parameterNames = request.getParameterNames();
        int paramOrder = 0;
        while (parameterNames.hasMoreElements()) {
            String parameterName = parameterNames.nextElement();
            paramOrder++;
            log.error("{}、参数名：{} <===> 参数值：{}", paramOrder, parameterName, request.getParameter(parameterName));
        }
    }
}
