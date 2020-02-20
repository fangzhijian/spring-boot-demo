package com.example.boot.config;

import com.example.boot.listener.rabbit.RabbitCallback;
import com.example.boot.util.DateUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * 2018/10/10 15:40
 * 走路呼呼带风
 */
@Configuration
@MapperScan(basePackages = "com.example.boot.mapper",annotationClass = Repository.class)
@PropertySource({"classpath:config/${spring.profiles.active}/base.properties"})
@Slf4j
@Import(RabbitCallback.class)
public class Config {

    /**
     * 设置jackson的objectMapper格式
     */
    private ObjectMapper initObjectMapper(){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(DateUtil.yyyy_MM_ddHH_mm_ss);
        objectMapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        objectMapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);
        //反序列化时json比model多余字段不会报错
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        //支持jdk8时间格式分别为yyyy-MM-dd HH:mm:ss,yyyy-MM-dd,HH:mm:ss
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateUtil.DATE_TIME_FORMATTER));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateUtil.DATE_TIME_FORMATTER));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateUtil.DATE_FORMATTER));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateUtil.DATE_FORMATTER));
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateUtil.TIME_FORMATTER));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateUtil.TIME_FORMATTER));
        objectMapper.registerModule(javaTimeModule).registerModule(new ParameterNamesModule()).registerModule(new Jdk8Module());
        return objectMapper;
    }

    /**
     * jackson不带记录全类名,可以被其他语言解析
     */
    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        return this.initObjectMapper();
    }

    /**
     * json记录全类名,可以被通用Object类反序列化
     */
    @Bean(name = "commonObjectMapper")
    public ObjectMapper commonObjectMapper(){
        ObjectMapper objectMapper = this.initObjectMapper();
        //记录class,使之能反序列各种复杂结构
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        objectMapper.findAndRegisterModules();
        return objectMapper;
    }

    /**
     * 修改RedisTemplate使用jackson序列化
     * redis key值使用String类型,操作hash时map的key值也要使用String
     */
    @Bean(name = "redisTemplate")
    @Primary
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory,@Qualifier("commonObjectMapper") ObjectMapper objectMapper) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setEnableTransactionSupport(true);
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        template.setHashKeySerializer(stringRedisSerializer);
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.setKeySerializer(stringRedisSerializer);
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }

    @Bean(name = "redisTemplateDB3")
    public RedisTemplate<String, Object> redisTemplateDB3(@Qualifier("lettuceConnectionDB3") RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setEnableTransactionSupport(true);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        template.setHashKeySerializer(stringRedisSerializer);
        template.setKeySerializer(stringRedisSerializer);
        log.warn("redis db {}","3");
        return template;
    }

    /**
     * spring缓存使用Redis
     */
    @Bean
    public RedisCacheManager redisCacheManager(RedisTemplate<String, Object> redisTemplate, RedisConnectionFactory redisConnectionFactory) {
        return new RedisCacheManager(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory),
                //未指定cacheNames使用默认时间5小时
                this.getRedisCacheConfigurationWithTtl(3600L*5,redisTemplate),
                //指定cacheNames自定义过期时间
                this.getRedisCacheConfigurationMap(redisTemplate));
    }

    /**
     * 修改特定cacheNames的缓存时间
     */
    private Map<String, RedisCacheConfiguration> getRedisCacheConfigurationMap(RedisTemplate<String, Object> redisTemplate) {
        Map<String, RedisCacheConfiguration> redisCacheConfigurationMap = new HashMap<>();
        redisCacheConfigurationMap.put("user", this.getRedisCacheConfigurationWithTtl(3000L,redisTemplate));
        return redisCacheConfigurationMap;
    }

    /**
     *  redis缓存配置
     */
    private RedisCacheConfiguration getRedisCacheConfigurationWithTtl(Long seconds,RedisTemplate<String, Object> redisTemplate) {
        return RedisCacheConfiguration.defaultCacheConfig()
                //使用RedisTemplate的Jackson序列化
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisTemplate.getValueSerializer()))
                .disableCachingNullValues()
                //过期时间
                .entryTtl(Duration.ofSeconds(seconds))
                //统一使用前缀cache,格式 cache:{cacheNames}:{key}
                .computePrefixWith((x)->String.format("cache:%s:",x));
    }

    /**
     * rest Http请求工具
     */
    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory factory) {
        RestTemplate restTemplate = new RestTemplate(factory);
        List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
        messageConverters.forEach((x) -> {
            if (x instanceof StringHttpMessageConverter) {
                ((StringHttpMessageConverter) x).setDefaultCharset(Charset.forName("UTF-8"));
            }
        });
        return restTemplate;
    }

    /**
     * rest Http连接池
     */
    @Bean
    public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(5000);//单位为ms
        factory.setConnectTimeout(5000);//单位为ms
        return factory;
    }

    /**
     * rabbitMQ和RabbitListener序列化使用jackson
     * @param connectionFactory mq连接工厂
     * @param rabbitTemplate    rabbit模板工具
     * @param objectMapper      Jackson序列化mapper
     * @param rabbitCallback    失败策略
     */
    @Bean
    @ConditionalOnBean({RabbitCallback.class,RabbitTemplate.class,ConnectionFactory.class})
    public RabbitListenerContainerFactory<?> rabbitListenerContainerFactory(ConnectionFactory connectionFactory, RabbitTemplate rabbitTemplate,
                                                                            @Qualifier("commonObjectMapper") ObjectMapper objectMapper, RabbitCallback rabbitCallback){
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        //使用jackson序列化
        Jackson2JsonMessageConverter messageConverter = new Jackson2JsonMessageConverter(objectMapper);
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter);
        factory.setMaxConcurrentConsumers(1000);
        factory.setConcurrentConsumers(100);
        factory.setReceiveTimeout(10000L);
        //1、发送端到exchange交换器
        rabbitTemplate.setConfirmCallback(rabbitCallback);
        //2、exchange到queue队列
        rabbitTemplate.setReturnCallback(rabbitCallback);
        //3、queue队列到消费端失败采用手动ack应答
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        rabbitTemplate.setMessageConverter(messageConverter);
        return factory;
    }


    /**
     * Converter无法使用lambda注入bean,等待spring优化
     * jdk8时间入参必须加@RequestParam,因为时间类无法new,等待spring优化
     * 入参yyyy-MM-dd HH:mm:ss接收Date
     */
    @Bean
    public Converter<String,Date> dateConverter(){
        return new Converter<String, Date>() {
            @Nullable
            @Override
            public Date convert(@Nullable String s) {
                if (!StringUtils.hasText(s)){
                    return null;
                }
                try {
                    return DateUtil.yyyy_MM_ddHH_mm_ss.parse(s);
                } catch (ParseException e) {
                    return null;
                }
            }
        };
    }


    /**
     * 入参yyyy-MM-dd HH:mm:ss接收LocalDateTime
     */
    @Bean
    public Converter<String, LocalDateTime> LocalDateTimeConvert() {
        return new Converter<String, LocalDateTime>() {
            @Nullable
            @Override
            public LocalDateTime convert(@Nullable String s) {
                if (!StringUtils.hasText(s)){
                    return null;
                }
                return LocalDateTime.parse(s,DateTimeFormatter.ofPattern(DateUtil.DATE_TIME));
            }
        };
    }

    /**
     * 入参yyyy-MM-dd接收为LocalDate
     */
    @Bean
    public Converter<String,LocalDate> localDateConverter(){
        return new Converter<String, LocalDate>() {
            @Nullable
            @Override
            public LocalDate convert(@Nullable String s) {
                if (!StringUtils.hasText(s)){
                    return null;
                }
                return LocalDate.parse(s,DateTimeFormatter.ofPattern(DateUtil.DATE));
            }
        };
    }

    /**
     * 入参HH:mm:ss接收为LocalTime
     */
    @Bean
    public Converter<String,LocalTime> localTimeConverter(){
        return new Converter<String, LocalTime>() {
            @Nullable
            @Override
            public LocalTime convert(@Nullable String s) {
                if (!StringUtils.hasText(s)){
                    return null;
                }
                return LocalTime.parse(s,DateTimeFormatter.ofPattern(DateUtil.TIME));
            }
        };
    }

    /**
     *  使用Lettuce配置redis连接工厂1
     */
    @Bean(name = "LettuceConnectionFactory")
    @Primary
    public LettuceConnectionFactory lettuceConnectionFactory1(RedisProperties redisProperties){
        return new LettuceConnectionFactory(getSentinelConfiguration(redisProperties,redisProperties.getDatabase()),
                                            getClientConfiguration(redisProperties));
    }

    /**
     *  使用Lettuce配置redis连接工厂2
     */
    @Bean(name = "lettuceConnectionDB3")
    public LettuceConnectionFactory lettuceConnectionFactory2(RedisProperties redisProperties){
        return new LettuceConnectionFactory(getSentinelConfiguration(redisProperties,3),
                                            getClientConfiguration(redisProperties));
    }

    /**
     *  配置哨兵节点信息
     */
    private RedisSentinelConfiguration getSentinelConfiguration(RedisProperties redisProperties,int database){
        RedisProperties.Sentinel sentinelProperties = redisProperties.getSentinel();
        RedisSentinelConfiguration config = new RedisSentinelConfiguration();
        config.master(sentinelProperties.getMaster());
        //设置节点
        List<RedisNode> nodes = new ArrayList<>();
        RedisProperties.Sentinel sentinel = redisProperties.getSentinel();
        for (String node : sentinel.getNodes()) {
            String[] parts = StringUtils.split(node, ":");
            if (parts != null){
                nodes.add(new RedisNode(parts[0], Integer.valueOf(parts[1])));
            }
        }
        config.setSentinels(nodes);

        if (redisProperties.getPassword() != null) {
            config.setPassword(RedisPassword.of(redisProperties.getPassword()));
        }
        config.setDatabase(database);
        return config;
    }

    /***
     *  配置Lettuce连接池属性
     */
    private LettuceClientConfiguration getClientConfiguration(RedisProperties redisProperties){
        GenericObjectPoolConfig<?> config = new GenericObjectPoolConfig<>();
        RedisProperties.Pool properties = redisProperties.getLettuce().getPool();
        config.setMaxTotal(properties.getMaxActive());
        config.setMaxIdle(properties.getMaxIdle());
        config.setMinIdle(properties.getMinIdle());
        if (properties.getMaxWait() != null) {
            config.setMaxWaitMillis(properties.getMaxWait().toMillis());
        }
        LettuceClientConfiguration.LettuceClientConfigurationBuilder builder = LettucePoolingClientConfiguration.builder()
                .poolConfig(config);
        if (redisProperties.isSsl()) {
            builder.useSsl();
        }
        if (redisProperties.getTimeout() != null) {
            builder.commandTimeout(redisProperties.getTimeout());
        }
        if (redisProperties.getLettuce() != null) {
            RedisProperties.Lettuce lettuce = redisProperties.getLettuce();
            if (lettuce.getShutdownTimeout() != null && !lettuce.getShutdownTimeout().isZero()) {
                builder.shutdownTimeout(redisProperties.getLettuce().getShutdownTimeout());
            }
        }
        return builder.build();
    }
}