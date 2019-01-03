package com.example.boot.util;

import com.google.gson.Gson;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.lang.Nullable;
import java.nio.charset.Charset;

/**
 * 2019/1/3 20:30
 * 走路呼呼带风
 */
public class GSonRedisSerializer<T>  implements RedisSerializer<T>{

    private Gson gson;

    private Class<T> tClass;

    public GSonRedisSerializer(Class<T> tClass,Gson gson){
            this.tClass = tClass;
            this.gson = gson;
    }

    @Nullable
    @Override
    public byte[] serialize(@Nullable T t) throws SerializationException {
        return gson.toJson(t).getBytes(Charset.forName("UTF-8"));
    }

    @Nullable
    @Override
    public T deserialize(@Nullable byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length==0){
            return null;
        }
        return gson.fromJson(new String(bytes, Charset.forName("UTF-8")),tClass);
    }
}
