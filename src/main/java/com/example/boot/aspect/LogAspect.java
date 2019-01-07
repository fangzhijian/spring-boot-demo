package com.example.boot.aspect;

import com.example.boot.errorCode.PubError;
import com.example.boot.exception.BusinessException;
import com.example.boot.model.InfoJson;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
/**
 * 2018/9/18 9:52
 * 走路呼呼带风
 * 用于生成方法出入参日志
 */
@Aspect
@Component
@Slf4j
public class LogAspect {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;


    /**
     * 扫描所有带 @MethodLog 和 @RequestMapping 注解的方法
     *   和以 Impl结尾的实现类,实现类中不是继承接口的方法并不会扫描
     *   作用:日志中打印出入参
     */
    @Around("@annotation(com.example.boot.aspect.MethodLog) || @annotation(org.springframework.web.bind.annotation.RequestMapping) " +
            "|| execution(* com.example.boot..*Impl.*(..)) || @annotation(org.springframework.web.bind.annotation.GetMapping) ||" +
            "@annotation(org.springframework.web.bind.annotation.PostMapping) || @annotation(org.springframework.web.bind.annotation.PutMapping) ||" +
            "@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
    public Object productLog(ProceedingJoinPoint joinPoint ) throws Throwable {

        Class<?> clazz = joinPoint.getTarget().getClass();
        String methodName = joinPoint.getSignature().getName();
        MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
        Class[]  parameterTypes = methodSignature.getParameterTypes();
        Method method;
        String repeatLockKey = null;            //防重复锁的key
        String resourceLockKey = null;          //资源锁的key
        boolean deleteLockAsFinish = false;     //是否在方法结束时删除防重复锁
        boolean methodAnnotationRepeatLock;     //方法上是否标明放重复锁注解
        boolean methodAnnotationResourceLock;   //方法上是否标明资源所注解
        try {

            //拼写入参
            method = clazz.getMethod(methodName,parameterTypes);
            Object[] parameterValues = joinPoint.getArgs();
            String[] parameterNames = methodSignature.getParameterNames();
            Map<String,Object> paramMap = new HashMap<>(parameterNames.length);
            StringBuilder sb = new StringBuilder();
            sb.append("方法").append(clazz.getName()).append(".").append(methodName).append("入参:");
            for (int i = 0; i <parameterNames.length ; i++) {
                sb.append(i).append("、").append(parameterNames[i]).append("--").append(parameterValues[i]).append("。");
                paramMap.put(parameterNames[i],parameterValues[i]);
            }

            if (method.isAnnotationPresent(MethodLog.class)){
                MethodLog methodLog = method.getAnnotation(MethodLog.class);
                if (methodLog.value() == LogType.AROUND  || methodLog.value() == LogType.BEFORE){
                    log.info(sb.toString());
                }else {
                    log.info("方法{}.{}已进入",clazz.getName(),methodName);
                }
            }else {
                log.info(sb.toString());
            }
            //判断redis防重复锁
            methodAnnotationRepeatLock = method.isAnnotationPresent(RepeatLock.class);
            if (methodAnnotationRepeatLock){
                RepeatLock repeatLock = AnnotationUtils.findAnnotation(method, RepeatLock.class);
                if (repeatLock != null){
                    deleteLockAsFinish = repeatLock.deleteFinish();
                    repeatLockKey = getLockKey("repeat:",repeatLock.prefixKey(), repeatLock.suffixKey(),clazz.getSimpleName(),methodName,paramMap);
                    Boolean getLock =redisTemplate.opsForValue().setIfAbsent(repeatLockKey,1, repeatLock.expireTime(), repeatLock.timeUnit());
                    if (getLock == null || !getLock){
                        return InfoJson.setFailed(PubError.P2006_REPEAT_CLICK.code(),PubError.P2006_REPEAT_CLICK.message());
                    }
                }
            }
            //判断redis资源竞争锁
            methodAnnotationResourceLock = method.isAnnotationPresent(ResourceLock.class);
            if (methodAnnotationResourceLock){
                ResourceLock resourceLock = AnnotationUtils.findAnnotation(method,ResourceLock.class);
                if (resourceLock != null){
                    resourceLockKey = getLockKey("resource:",resourceLock.prefixKey(),resourceLock.suffixKey(),clazz.getSimpleName(),methodName,paramMap);
                    try {
                        tryGetResourceLock(resourceLockKey,resourceLock.expireTime(),resourceLock.timeUnit(), resourceLock.interVal(),resourceLock.timeOut());
                    }catch (BusinessException e){
                        return InfoJson.setFailed(e.getCode(),e.getMessage());
                    }
                }
            }

        } catch (NoSuchMethodException e) {
            log.error(e.getMessage(),e);
            throw e;
        }
        try {

            Object  result= joinPoint.proceed();
            //拼写出参
            if (result == null){
                log.info("方法{}.{}已结束",clazz.getName(),methodName);
            }else {
                if (method.isAnnotationPresent(MethodLog.class)){
                    MethodLog methodLog = method.getAnnotation(MethodLog.class);
                    if (methodLog.value() == LogType.AROUND || methodLog.value() == LogType.AFTER){
                        log.info("方法{}.{}出参:{}",clazz.getName(),methodName,result.toString());
                    }else if (methodLog.value() == LogType.BEFORE){
                        log.info("方法{}.{}已结束",clazz.getName(),methodName);
                    }
                }else {
                    log.info("方法{}.{}出参:{}",clazz.getName(),methodName,result.toString());
                }


            }

            return result;
        } catch (Throwable throwable) {
            log.error(throwable.getMessage(),throwable);
            throw throwable;
        }finally {
            if (methodAnnotationRepeatLock && repeatLockKey != null && deleteLockAsFinish){
                redisTemplate.delete(repeatLockKey);
            }
            if (methodAnnotationResourceLock && resourceLockKey != null){
                redisTemplate.delete(resourceLockKey);
            }
        }


    }

    //获取注解中redis的key值
    private String getLockKey(String lockName,String prefixKey,String suffixKey,String className,String methodName,Map<String,Object> paramMap) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String lockKey;
        if (!StringUtils.hasText(prefixKey)){
            prefixKey = "lock:"+lockName+className+":"+methodName+":";
        }else {
            prefixKey = "lock:"+lockName+prefixKey;
        }
        if (suffixKey.matches("^#(\\w|\\d|_)+")){
            lockKey = prefixKey+ paramMap.get(suffixKey.substring(1));
        }else if (suffixKey.matches("^#(\\w|\\d|_)+\\.(\\w|\\d|_)+$")){
            String[] split = suffixKey.substring(1).split("\\.");
            Object paramValue = paramMap.get(split[0]);
            Class<?> clazz = paramValue.getClass();
            String getFieldMethod = getFieldMethod(split[1]);
            Method method = clazz.getDeclaredMethod(getFieldMethod);
            method.setAccessible(true);
            Object invoke = method.invoke(paramValue);
            lockKey = prefixKey+invoke;
        }else {
            lockKey = prefixKey+suffixKey;
        }
        return lockKey;
    }

    //获取成员变量的get方法名
    private String getFieldMethod(String fieldName){
        //首字母大写
        char[] chars = fieldName.toCharArray();
        if (chars[0] >= 'a' && chars[0] <= 'z') {
            chars[0] = (char)(chars[0] - 32);
        }
        return "get"+new String(chars);
    }

    //周期性尝试,直到获取到锁或者超过额定时间
    private void tryGetResourceLock(String resourceLockKey, long expireTime, TimeUnit timeUnit,int interVal,int timeOut) throws InterruptedException, BusinessException {
        timeOut = timeOut<1500?1500:timeOut;
        long startTime = System.currentTimeMillis();
        Boolean getLock;
        while ((getLock = redisTemplate.opsForValue().setIfAbsent(resourceLockKey,1,expireTime,timeUnit)) !=null && !getLock){
            if (System.currentTimeMillis()-startTime>timeOut+interVal){
                throw new BusinessException(PubError.P2007_RESOURCE_CLICK.code(),PubError.P2007_RESOURCE_CLICK.message());
            }
            Thread.sleep(interVal);
        }
    }
}
