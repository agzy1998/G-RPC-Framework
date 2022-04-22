package test1;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class Test {
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        User user1 = new User("123", "zhangsan");
        byte[] bytes = JSON.toJSONString(user1).getBytes();
        UserEnhancer userEnhancer = new UserEnhancer(user1);
        User user = (User) userEnhancer.getProxy(User.class);
        user.out("asdfsa", "dsafa");
    }
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class User{
    String id;
    String name;

    public void out(String id, String name){
        System.out.println(id +" : " +name);
    }
}

class UserEnhancer implements InvocationHandler{
    private final Object target;

    UserEnhancer(Object target) {
        this.target = target;
    }

    public Object getProxy(Class clazz){
        return Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, this);
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println(1);
        return method.invoke(target, args);
    }
}