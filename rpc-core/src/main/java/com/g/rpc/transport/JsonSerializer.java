package com.g.rpc.transport;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.g.rpc.entity.RPCRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class JsonSerializer implements CommonSerializer{
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(Object obj) {
        try{
            return objectMapper.writeValueAsBytes(obj);
        } catch (JsonProcessingException e){
            log.error("序列化错误");
            return null;
        }
    }

    @Override
    public Object deserialize(byte[] bytes, Class<?> clazz) {
        try{
            Object obj = objectMapper.readValue(bytes, clazz);
            log.info(String.valueOf(obj instanceof RPCRequest));
            if(obj instanceof RPCRequest){
                obj = handleRequest(obj);
            }
            return obj;
        } catch (StreamReadException e) {
            e.printStackTrace();
        } catch (DatabindException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 由于使用JSON序列化和反序列化Object数组, 无法保证反序列化仍为原实例类型, 需要重新判断处理
     * @return
     */
    private Object handleRequest(Object obj) throws IOException {
        RPCRequest rpcRequest = (RPCRequest) obj;
        for (int i = 0;i < rpcRequest.getParamTypes().length; i++){
            Class<?> clazz = rpcRequest.getParamTypes()[i];
            // isAssignableFrom确定由此类对象表示的类/接口是否与指定的Class类表示的接口相同 或者是 超类 或 类接口
            if(!clazz.isAssignableFrom(rpcRequest.getParameters()[i].getClass())) {
                byte[] bytes = objectMapper.writeValueAsBytes(rpcRequest.getParameters()[i]);
                rpcRequest.getParameters()[i] = objectMapper.readValue(bytes, clazz);
            }
        }
        return rpcRequest;
    }

    @Override
    public int getCode() {
        return 1;
    }
}
