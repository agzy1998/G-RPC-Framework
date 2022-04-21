import com.g.rpc.api.HelloService;
import com.g.rpc.entity.DefaultServiceRegistry;
import com.g.rpc.entity.RPCServer;
import com.g.rpc.entity.ServiceRegistry;


public class TestServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        ServiceRegistry serviceRegistry = new DefaultServiceRegistry();
        serviceRegistry.register(helloService);
        RPCServer rpcServer = new RPCServer(serviceRegistry);
        rpcServer.start(9000);

    }
}
