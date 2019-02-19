package com.example.boot.util;

import com.example.boot.aspect.Property;
import com.example.boot.listener.rabbit.CorrelationMessage;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringValueResolver;
import java.lang.reflect.Field;

/**
 * 2019/1/31 17:33
 * 走路呼呼带风
 */
@Component
public class SpringUtil implements EmbeddedValueResolverAware,ApplicationContextAware,BeanPostProcessor {


    private static StringValueResolver stringValueResolver;
    private static ApplicationContext applicationContext;
    private static RabbitTemplate rabbitTemplate;

    /**
     *  用于发送 rabbitMQ , 包装一层CorrelationMessage。
     *  CorrelationMessage继承于CorrelationData,主要作用是由于
     *  消息发送到 exchange失败时无法获取发送消息,而CorrelationData只能设置一个id
     *  本项目MQ从 1、发送端到exchange交换器 2、exchange到queue队列 失败时，记录数据库
     *             3、queue队列到消费端失败采用手动ack应答
     *  当使用 amqpTemplate和rabbitTemplate工具发送时,步骤1失败不会做失败策略
     * @see org.springframework.amqp.core.AmqpTemplate
     * @see org.springframework.amqp.rabbit.core.RabbitTemplate
     * @param exchange          交换器
     * @param routingKey        路由键
     * @param object            消息实体
     */
    public static void sendRabbitMQ(String exchange, String routingKey, final Object object){
        MessageProperties messageProperties = new MessageProperties();
        //messageId=0表示新发送的消息,还未失败,未进行获取自增失败id
        messageProperties.setMessageId("0");
        Message message = rabbitTemplate.getMessageConverter().toMessage(object,messageProperties );
        CorrelationMessage correlationMessage = new CorrelationMessage(exchange,routingKey,message.getBody(),"0");
        rabbitTemplate.send(exchange,routingKey,message,correlationMessage);
    }

    /**
     * @see org.springframework.beans.factory.annotation.Value
     * @param key   SPel表达式写法,相当@Value取值,但是能设置静态变量和常量,任何地方
     * @return      获取spring环境中的值,包含配置文件和系统环境等
     */
    public static String getValue(String key){
        return stringValueResolver.resolveStringValue(key);
    }

    /**
     * 同getValue方法,只是省去了${}
     * @param key   ${}取值表达式
     * @return      获取spring环境中的值,包含配置文件和系统环境等
     */
    public static String getProperty(String key){
        try {
            return stringValueResolver.resolveStringValue(String.format("${%s}",key));
        }catch (Exception e){
            return null;
        }
    }

    /**
     * 同getValue方法,只是省去了${}
     * @param key   ${}取值表达式
     * @param check 为true时,当key在配置中不存在启动报错
     * @return      获取spring环境中的值,包含配置文件和系统环境等
     */
    public static String getProperty(String key,boolean check){
        try {
            return stringValueResolver.resolveStringValue(String.format("${%s}",key));
        }catch (Exception e){
            if (check){
                throw e;
            }
            return null;
        }
    }


    public static ApplicationContext getApplicationContext(){
        return applicationContext;
    }

    public static <T>T getBean(Class<T> clazz){
        return applicationContext.getBean(clazz);
    }
    public static Object getBean(String beanName){
        return applicationContext.getBean(beanName);
    }
    public static void publishEvent(ApplicationEvent event){
        applicationContext.publishEvent(event);
    }

    @Override
    public void setEmbeddedValueResolver(@Nullable StringValueResolver resolver) {
        if (resolver == null){
            throw new BeanInitializationException("StringValueResolver初始化时为空");
        }
        SpringUtil.stringValueResolver = resolver;
    }

    @Override
    public void setApplicationContext(@Nullable ApplicationContext applicationContext) throws BeansException {
        if (applicationContext == null){
            throw new BeanInitializationException("StringValueResolver初始化时为空");
        }
        SpringUtil.applicationContext = applicationContext;
        SpringUtil.rabbitTemplate = applicationContext.getBean(RabbitTemplate.class);
    }

    @Override
    public Object postProcessBeforeInitialization(@Nullable Object bean, String beanName) throws BeansException {
        if (bean == null){
            return null;
        }
        //对标注了Property注解的成员变量赋值
        try {
            assignmentField(bean,bean.getClass());
        }  catch (IllegalAccessException e) {
            throw new BeanInitializationException(String.format("Bean--%s使用@Property赋值时异常",beanName));
        }
        return bean;
    }

    /**
     * 获取bean类的所有基础类型成员变量,包含私有和父类成员变量
     */
    private void assignmentField(Object bean,Class<?> clazz) throws IllegalAccessException {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field:fields){
            field.setAccessible(true);
            if (field.isAnnotationPresent(Property.class)){
                Property property = field.getAnnotation(Property.class);
                String propertyValue = SpringUtil.getProperty(property.value(),property.check());
                if (propertyValue == null){
                    continue;
                }
                setField(bean,field,propertyValue);
            }
        }
        Class<?> superclass = clazz.getSuperclass();
        if (superclass != null && !"java.lang.Object".equals(superclass.getName())){
            assignmentField(bean,superclass);
        }
    }

    private void setField(Object bean,Field field,String propertyValue) throws IllegalAccessException {
        String fieldTypeName = field.getType().getName();
        switch (fieldTypeName){
            case "java.lang.String":
                field.set(bean,propertyValue);
                break;
            case "java.lang.Byte": case "byte":
                field.set(bean,Byte.parseByte(propertyValue));
                break;
            case "java.lang.Short": case "short":
                field.set(bean,Short.parseShort(propertyValue));
                break;
            case "java.lang.Integer": case "int":
                field.set(bean,Integer.parseInt(propertyValue));
                break;
            case "java.lang.Long":    case "long":
                field.set(bean,Long.parseLong(propertyValue));
                break;
            case "java.lang.Double":  case "double":
                field.set(bean,Double.parseDouble(propertyValue));
                break;
            case "java.lang.Float":   case "float":
                field.set(bean,Float.parseFloat(propertyValue));
                break;
            case "java.lang.Boolean": case "boolean":
                field.set(bean,Boolean.parseBoolean(propertyValue));
                break;
            default:
                throw new BeanInitializationException(String.format("Bean使用@Property赋值时不支持该类型%s",fieldTypeName));
        }
    }
}