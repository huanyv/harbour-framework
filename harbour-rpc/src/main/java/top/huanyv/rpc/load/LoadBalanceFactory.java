package top.huanyv.rpc.load;

import java.util.HashMap;
import java.util.Map;

/**
 * @author huanyv
 * @date 2023/1/20 14:36
 */
public class LoadBalanceFactory {

    private static final Map<Balance, LoadBalance> loadBalanceMap = new HashMap<>();

    static {
        loadBalanceMap.put(Balance.ROUND, new RoundLoadBalance());
        loadBalanceMap.put(Balance.RANDOM, new RandomLoadBalance());
    }

    public static LoadBalance getLoadBalance(Balance balance) {
        return loadBalanceMap.get(balance);
    }

}
