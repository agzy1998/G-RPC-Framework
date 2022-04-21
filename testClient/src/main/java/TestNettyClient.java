import com.g.rpc.api.HelloObject;
import com.g.rpc.api.HelloService;
import com.g.rpc.entity.RPCClient;
import com.g.rpc.nettyImpl.NettyClient;
import com.g.rpc.proxys.RPCClientProxy;

public class TestNettyClient {
    public static void main(String[] args) {
        RPCClient rpcClient = new NettyClient("127.0.0.1", 9000);
        RPCClientProxy rpcClientProxy = new RPCClientProxy(rpcClient);
        HelloService helloService = (HelloService) rpcClientProxy.getProxy(HelloService.class);
        HelloObject helloObject = new HelloObject("12312124", "2354sdfgsafa");
        String res = helloService.hello(helloObject);
        System.out.println(res);

    }
}
