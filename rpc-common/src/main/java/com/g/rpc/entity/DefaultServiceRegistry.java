package com.g.rpc.entity;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class DefaultServiceRegistry implements ServiceRegistry {
    /**
     * 存放Service, 用于get
     */
    private final Map<String, Object> serviceMap = new ConcurrentHashMap<>();
    /**
     * 注册Service, 避免重复注册
     */
    private final Set<String> registeredService = ConcurrentHashMap.newKeySet();

    @Override
    public synchronized <T> void register(T service) {
        String serviceName = service.getClass().getCanonicalName();
        if(registeredService.contains(serviceName)) {
            return;
        }
        registeredService.add(serviceName);
        Class<?>[] interfaces = service.getClass().getInterfaces();
        for (Class<?> i : interfaces){
            /**
             * 遍历当前类实现的所有接口, 并将 每个接口 : 当前实现类, 存入map
             */
            serviceMap.put(i.getCanonicalName(), service);
        }

        log.info("为接口: {} 注册服务: {}", interfaces, serviceName);
    }

    @Override
    public synchronized Object getService(String serviceName) {
        Object service = serviceMap.get(serviceName);
        if(service == null){
            log.info("服务不存在");
        }
        return service;
    }
}
