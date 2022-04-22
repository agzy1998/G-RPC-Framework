package com.g.rpc.transport;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

/**
 * fastJson 替换 jackson
 */
@Slf4j
public class JsonSerializer implements CommonSerializer{


    @Override
    public <T> byte[] serialize(T obj) {
        return JSON.toJSONString(obj).getBytes();

    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        return JSON.parseObject(new String(bytes), clazz);
    }

    @Override
    public int getCode() {
        return 1;
    }
}
