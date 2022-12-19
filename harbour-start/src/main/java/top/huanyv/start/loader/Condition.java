package top.huanyv.start.loader;

import top.huanyv.bean.ioc.ApplicationContext;
import top.huanyv.start.config.AppArguments;

/**
 * @author huanyv
 * @date 2022/12/19 13:13
 */
public interface Condition {

    boolean matchers(ApplicationContext applicationContext, AppArguments appArguments);

}
