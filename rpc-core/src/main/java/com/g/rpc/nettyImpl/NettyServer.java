package com.g.rpc.nettyImpl;

import com.g.rpc.entity.RPCServer;
import com.g.rpc.handler.NettyServerHandler;
import com.g.rpc.transport.CommonDecoder;
import com.g.rpc.transport.CommonEncoder;
import com.g.rpc.transport.JsonSerializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyServer implements RPCServer {


    @Override
    public void start(int port) {
        /**
         * 两个线程组
         * bossGroup 负责监听连接
         * workerGroup 负责处理客户端连接
         * 创建时并无区别, 在ServerBootstrap构建group是传入, 分配任务
         */
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        /**
         * 引导类, 引导服务端的启动工作
         */
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        /**
         * group负责配置上述两个线程组, bossGroup负责监听客户端请求, workerGroup负责处理每条连接的数据读写
         * channel 配置服务端的IO模型, NIO模式
         * childHandler 配置每条连接的数据读写和业务逻辑.
         * bind 绑定监听端口
         * child*  都是为处理客户端请求所做业务逻辑
         */
        try {
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>(){
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new CommonEncoder(new JsonSerializer()));
                            pipeline.addLast(new CommonDecoder());
                            pipeline.addLast(new NettyServerHandler());
                        }
                    });
            ChannelFuture future = serverBootstrap.bind(port).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
