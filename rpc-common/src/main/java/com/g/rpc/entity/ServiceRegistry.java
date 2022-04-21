package com.g.rpc.entity;

public interface ServiceRegistry {
    <T> void register(T service);
    Object getService(String serviceName);
}
