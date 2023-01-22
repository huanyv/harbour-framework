package top.huanyv.rpc.register;

import top.huanyv.rpc.load.Balance;
import top.huanyv.rpc.load.LoadBalance;
import top.huanyv.rpc.load.LoadBalanceFactory;

import java.util.List;

public abstract class AbstractRegistry implements Registry {
    protected static final String FOLDER = "/services";
    protected static final String SEPARATOR = "/";

    public String discover(String service, Balance loadBalance) {
        // 获取所有的 服务提供者【地址】
        List<String> providers = lookup(service);
        // TODO 负载均衡
        LoadBalance loadbalance = LoadBalanceFactory.getLoadBalance(loadBalance);
        return loadbalance.select(providers);
    }

    protected abstract void init(String address);

    protected abstract List<String> lookup(String service);
}
