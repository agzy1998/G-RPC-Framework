import com.g.rpc.api.HelloObject;
import com.g.rpc.api.HelloService;
import com.g.rpc.proxys.RPCClientProxy;

public class TestClient {
    public static void main(String[] args) {
        RPCClientProxy proxy = new RPCClientProxy("127.0.0.1", 9000);
        HelloService helloService = (HelloService) proxy.getProxy(HelloService.class);
        HelloObject helloObject = new HelloObject("123", "hello world");
        String res = helloService.hello(helloObject);
        System.out.println(res);
    }
}
