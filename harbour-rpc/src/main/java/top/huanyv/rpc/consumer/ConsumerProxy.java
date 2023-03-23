package top.huanyv.rpc.consumer;

import top.huanyv.rpc.load.Balance;
import top.huanyv.rpc.provider.NettyProviderServer;
import top.huanyv.rpc.provider.ProviderServer;
import top.huanyv.rpc.provider.RequestDTO;
import top.huanyv.rpc.register.Registry;
import top.huanyv.rpc.register.ZookeeperRegistry;
import top.huanyv.rpc.util.RpcConfigurer;
import top.huanyv.tools.utils.StringUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ConsumerProxy {

    private static final Registry registry = RpcConfigurer.create().getRegistry();

    private static final ConsumerServer server = new NettyConsumerServer();

    public static <T> T create(final Class<T> interfaceClass) {
        return create(interfaceClass, Balance.ROUND);
    }

    public static <T> T create(Class<T> interfaceClass, Balance loadBalance) {
        return create(interfaceClass, loadBalance, null);
    }

    /**
     * 创建消费者使用的代理类
     *
     * @param interfaceClass 接口服务
     * @param loadBalance    负载均衡
     * @param serviceName    服务名称
     * @return {@link T}
     */
    public static <T> T create(Class<T> interfaceClass, Balance loadBalance, String serviceName) {
        Object o = Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass}
                , new InvocationHandler() {
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        String finalServiceName = StringUtil.hasText(serviceName) ? serviceName : interfaceClass.getName();
                        // 封装调用参数
                        RequestDTO requestDTO = new RequestDTO();
                        requestDTO.setServiceName(finalServiceName);
                        requestDTO.setMethodName(method.getName());
                        requestDTO.setMethodParameterTypes(method.getParameterTypes());
                        requestDTO.setMethodArgs(args);

                        // 获取服务提供者
                        String serviceAddress = registry.discover(finalServiceName, loadBalance);

                        // 远程调用
                        return server.execute(serviceAddress, requestDTO);
                    }
                });
        return (T) o;
    }
}
