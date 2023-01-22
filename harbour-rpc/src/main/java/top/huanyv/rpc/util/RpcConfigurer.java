package top.huanyv.rpc.util;

import top.huanyv.rpc.register.Registry;

/**
 * @author huanyv
 * @date 2023/1/21 16:22
 */
public class RpcConfigurer {

    // 单例
    private RpcConfigurer() { }
    public static RpcConfigurer create() {
        return SingletonHolder.CONFIGURATION;
    }
    private static class SingletonHolder {
        public static final RpcConfigurer CONFIGURATION = new RpcConfigurer();
    }

    /**
     * 注册中心
     */
    private Registry registry;

    public void setRegistry(Registry registry) {
        this.registry = registry;
    }

    public Registry getRegistry() {
        if (this.registry == null) {
            throw new IllegalArgumentException("registry is not configured.");
        }
        return registry;
    }
}
