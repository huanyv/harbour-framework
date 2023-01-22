package top.huanyv.rpc.load;

import java.util.List;
import java.util.Random;

public class RandomLoadBalance extends AbstractLoadbalance {
    @Override
    public String doSelect(List<String> providers) {
        int len = providers.size();
        Random random = new Random();
        int lucky = random.nextInt(len);
        return providers.get(lucky);
    }
}
