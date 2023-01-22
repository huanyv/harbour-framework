package top.huanyv.rpc.load;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class RoundLoadBalance extends AbstractLoadbalance {

    private int num = 0;

    @Override
    public String doSelect(List<String> providers) {
        String provider = providers.get(num);
        num++;
        if (num >= providers.size()) {
            num = 0;
        }
        return provider;
    }
}
