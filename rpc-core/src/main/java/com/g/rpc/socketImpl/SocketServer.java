package com.g.rpc.socketImpl;

import com.g.rpc.handler.RequestHandler;
import com.g.rpc.handler.RequestHandlerThread;
import com.g.rpc.registry.ServiceRegistry;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

@Slf4j
public class SocketServer {
    private final int corePoolSize = 5;
    private final int maxPoolSize = 50;
    private final long keepAliveTime = 60;
    private final BlockingQueue<Runnable> workingQueue;
    private final ThreadFactory threadFactory;
    private final ExecutorService threadPool;
    private final ServiceRegistry serviceRegistry;
    private final RequestHandler requestHandler;

    public SocketServer(ServiceRegistry serviceRegistry){
        this.serviceRegistry = serviceRegistry;
        workingQueue = new ArrayBlockingQueue<>(100);
        threadFactory = Executors.defaultThreadFactory();
        requestHandler = new RequestHandler();
        threadPool = new ThreadPoolExecutor(corePoolSize,
                maxPoolSize,
                keepAliveTime,
                TimeUnit.SECONDS,
                workingQueue,
                threadFactory);
    }

    public void start(int port){
        try (ServerSocket serverSocket = new ServerSocket(port)){
            log.info("服务器正在启动....");
            Socket socket;
            while((socket = serverSocket.accept()) != null){
                log.info("客户端连接成功! IP: " + socket.getInetAddress());
                threadPool.execute(new RequestHandlerThread(socket, requestHandler, serviceRegistry));
            }
            threadPool.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
