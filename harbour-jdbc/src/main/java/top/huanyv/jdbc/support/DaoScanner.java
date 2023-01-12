package top.huanyv.jdbc.support;

import top.huanyv.bean.ioc.BeanDefinitionRegistry;
import top.huanyv.bean.ioc.BeanDefinitionRegistryPostProcessor;
import top.huanyv.bean.ioc.definition.BeanDefinition;
import top.huanyv.bean.ioc.definition.ClassBeanDefinition;
import top.huanyv.jdbc.annotation.Dao;
import top.huanyv.jdbc.core.JdbcConfigurer;
import top.huanyv.tools.utils.ClassUtil;
import top.huanyv.tools.utils.StringUtil;

import java.util.Set;

/**
 * @author admin
 * @date 2022/12/19 17:00:01
 */
public class DaoScanner implements BeanDefinitionRegistryPostProcessor {

    private String scanPackages;

    public DaoScanner() {
    }

    public DaoScanner(String scanPackages) {
        this.scanPackages = scanPackages;
    }

    public String getScanPackages() {
        return scanPackages;
    }

    public void setScanPackages(String scanPackages) {
        this.scanPackages = scanPackages;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) {
        if (scanPackages == null) {
            scanPackages = JdbcConfigurer.create().getScanPackages();
        }
        Set<Class<?>> classes = ClassUtil.getClassesByAnnotation(Dao.class, scanPackages);
        for (Class<?> cls : classes) {
            String beanName = StringUtil.firstLetterLower(cls.getSimpleName());
            BeanDefinition beanDefinition = new ClassBeanDefinition(DaoFactoryBean.class, cls);
            registry.register(beanName, beanDefinition);
        }
    }
}
