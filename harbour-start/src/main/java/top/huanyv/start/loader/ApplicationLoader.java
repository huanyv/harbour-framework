package top.huanyv.start.loader;

import top.huanyv.bean.ioc.ApplicationContext;
import top.huanyv.bean.ioc.Configuration;
import top.huanyv.bean.utils.Ordered;

/**
 * 应用程序加载器
 * 在应用启动时加载，使用JDK的SPI机制
 *
 * @author huanyv
 * @date 2022/12/17 16:30
 */
public interface ApplicationLoader extends Ordered {

    default void load(ApplicationContext applicationContext, Configuration configuration) {

    }

}
