package com.g.rpc.handler;

import com.g.rpc.entity.RPCResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyClientHandler extends SimpleChannelInboundHandler<RPCResponse> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RPCResponse rpcResponse) throws Exception {
        try {
            log.info("客户端收到消息:{}",rpcResponse.getData());
            AttributeKey<RPCResponse> key = AttributeKey.valueOf("rpcResponse");
            ctx.channel().attr(key).set(rpcResponse);
            ctx.close();
        } finally {
            ReferenceCountUtil.release(rpcResponse);
        }
    }
}
