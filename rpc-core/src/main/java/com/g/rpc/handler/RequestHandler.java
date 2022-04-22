package com.g.rpc.handler;

import com.alibaba.fastjson.JSON;
import com.g.rpc.entity.RPCRequest;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Slf4j
public class RequestHandler {
    public Object handle(RPCRequest rpcRequest, Object service) {
        Object result = null;
        log.info("开始处理request.......");
        result = invokeTargetMethod(rpcRequest, service);
        log.info("服务:{} 成功调用方法:{}", rpcRequest.getInterfaceName(), rpcRequest.getMethodName());
        log.info("处理结束..............");
        return result;
    }

    public Object invokeTargetMethod(RPCRequest rpcRequest, Object service){
        Method method = null;
        try{
            method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
            /**
             * fastJson转换后, 需对每个参数进行parse
             */
            Object[] params = rpcRequest.getParameters();
            Class[] clazzs = rpcRequest.getParamTypes();
            for(int i = 0;i < params.length;i++){
                params[i] = JSON.parseObject(String.valueOf(params[i]), clazzs[i]);
            }
            return method.invoke(service, params);
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
