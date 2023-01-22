package top.huanyv.rpc.test.provider;

import org.junit.Before;
import org.junit.Test;
import top.huanyv.bean.ioc.AnnotationConfigApplicationContext;
import top.huanyv.bean.ioc.ApplicationContext;
import top.huanyv.rpc.consumer.ConsumerProxy;
import top.huanyv.rpc.provider.NettyProviderServer;
import top.huanyv.rpc.provider.ProviderServer;
import top.huanyv.rpc.register.ZookeeperRegistry;
import top.huanyv.rpc.util.RpcConfigurer;

import java.io.IOException;

/**
 * @author huanyv
 * @date 2023/1/18 13:44
 */
public class ProviderTest {

    @Before
    public void config() {
        RpcConfigurer.create().setRegistry(new ZookeeperRegistry("127.0.0.1:2181"));
    }

    @Test
    public void test01() throws IOException {
        ProviderServer server = new NettyProviderServer("top.huanyv.rpc.test");
        server.start("127.0.0.1:20880");
        System.in.read();
    }


    @Test
    public void test02() {
        Service service = ConsumerProxy.create(Service.class);
        String msg = service.say("admin");
        System.out.println("msg = " + msg);

        System.out.println(service.say("张三"));
        System.out.println(service.say("张三"));
        System.out.println(service.say("张三"));
    }


    @Test
    public void test03() throws IOException {
        ApplicationContext app = new AnnotationConfigApplicationContext("top.huanyv.rpc.test");

        System.in.read();
    }
}
