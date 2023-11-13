package top.huanyv.start.loader;

import top.huanyv.bean.ioc.ApplicationContext;
import top.huanyv.bean.ioc.Configuration;

/**
 * @author huanyv
 * @date 2022/12/19 13:13
 */
public interface Condition {

    boolean matchers(ApplicationContext applicationContext, Configuration configuration);

}
