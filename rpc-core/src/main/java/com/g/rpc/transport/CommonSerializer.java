package com.g.rpc.transport;

public interface CommonSerializer {
    <T> byte[] serialize(T obj);

    <T> T deserialize(byte[] bytes, Class<T> clazz);

    int getCode();

    static CommonSerializer getByCode(int code){
        switch (code) {
            case 1:
                return new JsonSerializer();
            default:
                return null;
        }
    }
}
