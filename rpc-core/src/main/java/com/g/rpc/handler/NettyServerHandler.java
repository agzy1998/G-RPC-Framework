package com.g.rpc.handler;

import com.g.rpc.entity.RPCRequest;
import com.g.rpc.entity.RPCResponse;
import com.g.rpc.registry.DefaultServiceRegistry;
import com.g.rpc.registry.ServiceRegistry;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyServerHandler extends SimpleChannelInboundHandler<RPCRequest> {

    private static RequestHandler requestHandler;
    private static ServiceRegistry serviceRegistry;

    static{
        requestHandler = new RequestHandler();
        serviceRegistry = new DefaultServiceRegistry();
    }


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RPCRequest rpcRequest) throws Exception {
        try{
            log.info("服务器接收到请求: {} ", rpcRequest);
            String interfaceName = rpcRequest.getInterfaceName();
            Object service = serviceRegistry.getService(interfaceName);
            log.info("开始调用{} 处理请求{}",service, rpcRequest);
            Object result = requestHandler.handle(rpcRequest, service);
            ChannelFuture future = channelHandlerContext.writeAndFlush(RPCResponse.success(result));
            future.addListener(ChannelFutureListener.CLOSE);
        } finally {
            ReferenceCountUtil.release(rpcRequest);
        }
    }
}
