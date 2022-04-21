package com.g.rpc.entity;

import com.g.rpc.enums.ResponseCode;
import lombok.Data;

import java.io.Serializable;

@Data
public class RPCResponse<T> implements Serializable {
    /**
     * 响应状态吗
     */
    private Integer statusCode;

    /**
     * 响应状态补充信息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    public static <T> RPCResponse<T> success(T data){
        RPCResponse<T> response = new RPCResponse<T>();
        response.setStatusCode(ResponseCode.SUCCESS.getCode());
        response.setMessage(ResponseCode.SUCCESS.getMessage());
        response.setData(data);
        return response;
    }

    public static <T> RPCResponse<T> fail(T data){
        RPCResponse<T> response = new RPCResponse<T>();
        response.setStatusCode(ResponseCode.FAIL.getCode());
        response.setMessage(ResponseCode.FAIL.getMessage());
        return response;
    }
}
