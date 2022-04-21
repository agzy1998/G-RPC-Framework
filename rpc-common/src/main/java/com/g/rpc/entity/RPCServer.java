package com.g.rpc.entity;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

@Slf4j
public class RPCServer {
    private final ExecutorService threadPool;

    public RPCServer(){
        int corePoolSize = 5;
        int maxPoolSize = 50;
        long keepAliveTime = 60;
        BlockingQueue<Runnable> workingQueue = new ArrayBlockingQueue<>(100);
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        threadPool = new ThreadPoolExecutor(corePoolSize,
                maxPoolSize,
                keepAliveTime,
                TimeUnit.SECONDS,
                workingQueue,
                threadFactory);
    }

    public void register(Object service, int port){
        try (ServerSocket serverSocket = new ServerSocket(port)){
            log.info("服务器正在启动....");
            Socket socket;
            while((socket = serverSocket.accept()) != null){
                log.info("客户端连接成功! IP: " + socket.getInetAddress());
                threadPool.execute(new Task(socket, service));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
