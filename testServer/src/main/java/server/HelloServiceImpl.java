package server;

import com.g.rpc.api.HelloObject;
import com.g.rpc.api.HelloService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(HelloObject helloObject) {
        log.info("接收客户端数据: {} {}", helloObject.getMessage(), helloObject.getId());
        String res = helloObject.getId()+":"+helloObject.getMessage();
        return res;
    }
}
