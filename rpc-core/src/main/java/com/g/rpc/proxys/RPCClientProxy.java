package com.g.rpc.proxys;

import com.g.rpc.entity.RPCClient;
import com.g.rpc.entity.RPCRequest;
import com.g.rpc.entity.RPCResponse;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class RPCClientProxy<T> implements InvocationHandler {
    private String host;
    private int port;
    private RPCClient rpcClient;

    public RPCClientProxy(RPCClient rpcClient){
        this.rpcClient = rpcClient;
    }

    public RPCClientProxy(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public <T> T getProxy(Class<T> clazz){
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RPCRequest rpcRequest = RPCRequest.builder()
                .interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameters(args)
                .paramTypes(method.getParameterTypes()).build();

        return ((RPCResponse) rpcClient.sendRequest(rpcRequest)).getData();
    }
}
