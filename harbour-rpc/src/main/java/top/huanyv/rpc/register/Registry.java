package top.huanyv.rpc.register;

import top.huanyv.rpc.load.Balance;

public interface Registry {

    /**
     * 注册服务
     * @param providerAddress 服务提供者地址
     * @param service 服务
     */
    void register(String providerAddress, String service);

    String discover(String service, Balance loadBalance);
}
