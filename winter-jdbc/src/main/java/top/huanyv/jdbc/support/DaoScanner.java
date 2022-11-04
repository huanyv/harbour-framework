package top.huanyv.jdbc.support;

import top.huanyv.ioc.core.ApplicationContext;
import top.huanyv.ioc.core.ApplicationContextWeave;
import top.huanyv.jdbc.anno.Dao;
import top.huanyv.jdbc.core.DaoProxyHandler;
import top.huanyv.jdbc.core.JdbcConfigurer;
import top.huanyv.jdbc.core.ProxyFactory;
import top.huanyv.utils.ClassUtil;
import top.huanyv.utils.ReflectUtil;
import top.huanyv.utils.StringUtil;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author admin
 * @date 2022/7/22 15:54
 */
public class DaoScanner implements ApplicationContextWeave {

    private String scanPackages;

    public DaoScanner() {

    }

    public DaoScanner(String scanPackages) {
        this.scanPackages = scanPackages;
    }

    @Override
    public void createBeanInstanceAfter(ApplicationContext applicationContext) {
        if (scanPackages == null) {
            scanPackages = JdbcConfigurer.create().getScanPackages();
        }
        Set<Class<?>> classes = ClassUtil.getClassesByAnnotation(Dao.class, scanPackages);
        for (Class<?> cls : classes) {
            if (cls.isInterface()) {
                applicationContext.registerBean(StringUtil.firstLetterLower(cls.getSimpleName()), SqlContextFactoryBean.class, cls);
            } else {
                applicationContext.registerBean(cls);
            }
        }
        applicationContext.refresh();
    }

    public String getScanPackages() {
        return scanPackages;
    }

    public void setScanPackages(String scanPackages) {
        this.scanPackages = scanPackages;
    }
}
