package top.huanyv.jdbc.support;

import top.huanyv.bean.ioc.ApplicationContext;
import top.huanyv.bean.ioc.ApplicationContextWeave;
import top.huanyv.jdbc.annotation.Dao;
import top.huanyv.jdbc.core.JdbcConfigurer;
import top.huanyv.tools.utils.ClassUtil;
import top.huanyv.tools.utils.StringUtil;

import java.util.Set;

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
