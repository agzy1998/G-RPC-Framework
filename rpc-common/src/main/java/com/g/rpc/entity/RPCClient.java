package com.g.rpc.entity;

public interface RPCClient {
    Object sendRequest(RPCRequest rpcRequest);
}
