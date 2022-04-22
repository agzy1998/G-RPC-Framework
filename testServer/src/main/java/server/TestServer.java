package server;

import com.g.rpc.api.HelloService;
import com.g.rpc.socketImpl.SocketServer;
import com.g.rpc.registry.DefaultServiceRegistry;
import com.g.rpc.registry.ServiceRegistry;


public class TestServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        ServiceRegistry serviceRegistry = new DefaultServiceRegistry();
        serviceRegistry.register(helloService);
        SocketServer socketServer = new SocketServer(serviceRegistry);
        socketServer.start(9000);

    }
}
