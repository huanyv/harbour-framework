package top.huanyv.start.core;

import top.huanyv.bean.ioc.Configuration;
import top.huanyv.bean.utils.Ordered;

/**
 * @author huanyv
 * @date 2022/12/26 14:54
 */
public interface ApplicationRunner extends Ordered {
    void run(Configuration configuration);
}
