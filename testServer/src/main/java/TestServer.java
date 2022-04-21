import com.g.rpc.api.HelloService;
import com.g.rpc.entity.RPCServer;


public class TestServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        RPCServer rpcServer = new RPCServer();
        rpcServer.register(helloService, 9000);
    }
}
