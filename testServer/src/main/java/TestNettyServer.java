import com.g.rpc.api.HelloService;
import com.g.rpc.nettyImpl.NettyServer;
import com.g.rpc.registry.DefaultServiceRegistry;
import com.g.rpc.registry.ServiceRegistry;


public class TestNettyServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        ServiceRegistry serviceRegistry = new DefaultServiceRegistry();
        serviceRegistry.register(helloService);
        NettyServer server = new NettyServer();
        server.start(9000);
    }
}
