package com.example.boot.aspect;

import com.example.boot.errorCode.PubError;
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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

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
        String lockKey = null;
        boolean deleteLockAsFinish = true;
        boolean methodAnnotationLock;
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
                }
            }else {
                log.info(sb.toString());
            }
            //判断redis分布式锁
            methodAnnotationLock = method.isAnnotationPresent(RedisLock.class);
            if (methodAnnotationLock){
                RedisLock redisLock  = AnnotationUtils.findAnnotation(method, RedisLock.class);
                if (redisLock != null){
                    deleteLockAsFinish = redisLock.deleteFinish();
                    lockKey = getLockKey(redisLock.prefixKey(),redisLock.suffixKey(),methodName,paramMap);
                    Boolean getLock =redisTemplate.opsForValue().setIfAbsent(lockKey,1,redisLock.expireTime(),redisLock.timeUnit());
                    if (getLock == null || !getLock){
                        return InfoJson.setFailed(PubError.P2006_REPEAT_CLICK.code(),PubError.P2006_REPEAT_CLICK.message());
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
                log.info("方法{}.{}已结束",clazz.getName(),method.getName());
            }else {
                if (method.isAnnotationPresent(MethodLog.class)){
                    MethodLog methodLog = method.getAnnotation(MethodLog.class);
                    if (methodLog.value() == LogType.AROUND || methodLog.value() == LogType.AFTER){
                        log.info("方法{}.{}出参:{}",clazz.getName(),method.getName(),result.toString());
                    }else if (methodLog.value() == LogType.BEFORE){
                        log.info("方法{}.{}已结束",clazz.getName(),method.getName());
                    }
                }else {
                    log.info("方法{}.{}出参:{}",clazz.getName(),method.getName(),result.toString());
                }


            }
            if (methodAnnotationLock && lockKey != null && deleteLockAsFinish){
                log.info("我要删除redis锁了哦");
                redisTemplate.delete(lockKey);
            }
            return result;
        } catch (Throwable throwable) {
            log.error(throwable.getMessage(),throwable);
            if (methodAnnotationLock && lockKey != null && deleteLockAsFinish){
                log.info("我要删除redis锁了哦");
                redisTemplate.delete(lockKey);
            }
            throw throwable;
        }


    }

    //获取@RedisLcok注解中redis的key值
    private String getLockKey(String prefixKey,String suffixKey,String methodName,Map<String,Object> paramMap) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String lockKey;
        if ("lock:".equals(prefixKey)){
            prefixKey = prefixKey+methodName+":";
        }else {
            prefixKey = "lock:"+prefixKey;
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
}
