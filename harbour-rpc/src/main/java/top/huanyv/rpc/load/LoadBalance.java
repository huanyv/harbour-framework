package top.huanyv.rpc.load;

import java.util.List;

public interface LoadBalance {

    String ROUND = "round";
    String RANDOM = "random";

    String select(List<String> providers);
}
