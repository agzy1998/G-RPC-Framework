package com.g.rpc.nettyImpl;

import com.g.rpc.entity.RPCClient;
import com.g.rpc.entity.RPCRequest;
import com.g.rpc.entity.RPCResponse;
import com.g.rpc.handler.NettyClientHandler;
import com.g.rpc.transport.CommonDecoder;
import com.g.rpc.transport.CommonEncoder;
import com.g.rpc.transport.JsonSerializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyClient implements RPCClient {

    private String host;
    private int port;
    /**
     * 客户端引导启动类
     */
    public static final Bootstrap bootstrap;

    public NettyClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     *  静态代码块优先级最高, 静态代码块与静态变量按照声明顺序装载
     *  都是套接字channel, 故SocketChannel
     */
    static {
        EventLoopGroup group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>(){
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast(new CommonEncoder(new JsonSerializer()));
                        pipeline.addLast(new CommonDecoder());
                        pipeline.addLast(new NettyClientHandler());
                    }
                });
    }

    @Override
    public Object sendRequest(final RPCRequest rpcRequest) {
        try {
            ChannelFuture future = bootstrap.connect(host, port).sync();
            log.info("客户端连接到服务器 {}:{} ", host, port);
            Channel channel = future.channel();
            if(channel != null){
                channel.writeAndFlush(rpcRequest).addListener(future1 ->{
                    if(future1.isSuccess()){
                        log.info("客户端发送消息: {}", rpcRequest.toString());
                    }else{
                        log.info("发送时发生错误" , future1.cause());
                    }
                });
                // 上述writeAndFlush会另起一个子线程进行, 下述语句会阻塞当前线程, 直到子线程执行完毕
                channel.closeFuture().sync();
                // 获得给定的AtributeKey的值, 不会为空, 但可能返回没有值的Attribute, 只有调用get()才能获得其内部值
                AttributeKey<RPCResponse> key = AttributeKey.valueOf("rpcResponse");
                RPCResponse rpcResponse = channel.attr(key).get();
                return rpcResponse;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
