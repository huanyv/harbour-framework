package top.huanyv.webmvc.security;

import top.huanyv.bean.ioc.ApplicationContext;
import top.huanyv.bean.ioc.ApplicationContextAware;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author huanyv
 * @date 2023/3/23 20:18
 */
public class SubjectHolder implements ApplicationContextAware {

    private static ApplicationContext context;

    private static final Map<String, StorageStrategy> map = new ConcurrentHashMap<>(2);

    static {
        map.put(StorageStrategy.SESSION_STRATEGY, new SessionStorageStrategy());
        map.put(StorageStrategy.THREAD_LOCAL_STRATEGY, new ThreadLocalStorageStrategy());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        context = applicationContext;
    }

    public static StorageStrategy getStrategy() {
        if (context == null) {
            return new SessionStorageStrategy();
        }
        return context.getBean(StorageStrategy.class);
    }

}
