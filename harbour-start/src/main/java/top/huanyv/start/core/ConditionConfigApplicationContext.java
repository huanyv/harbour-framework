package top.huanyv.start.core;

import top.huanyv.bean.annotation.Bean;
import top.huanyv.bean.ioc.AbstractApplicationContext;
import top.huanyv.bean.ioc.AnnotationBeanDefinitionReader;
import top.huanyv.bean.ioc.ApplicationContext;
import top.huanyv.bean.ioc.definition.BeanDefinition;
import top.huanyv.bean.ioc.definition.MethodBeanDefinition;
import top.huanyv.bean.utils.OrderUtil;
import top.huanyv.start.anntation.Conditional;
import top.huanyv.start.anntation.ConfigurationProperties;
import top.huanyv.start.config.AppArguments;
import top.huanyv.start.loader.ApplicationLoader;
import top.huanyv.start.loader.Condition;
import top.huanyv.tools.utils.ReflectUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * @author huanyv
 * @date 2023/1/4 17:07
 */
public class ConditionConfigApplicationContext extends AbstractApplicationContext {

    private final AnnotationBeanDefinitionReader reader;

    public ConditionConfigApplicationContext(String... scanPackages) {
        super();
        reader = new AnnotationBeanDefinitionReader(getBeanDefinitionRegistry());
        reader.read(scanPackages);
        initialization();
    }

    /**
     * 调用加载器
     * 调用加载器，并加载所有条件Bean
     *
     * @param appArguments 应用参数
     */
    public void callLoaders(AppArguments appArguments) {
        List<ApplicationLoader> loaders = new ArrayList<>();
        // 获取SPI，应用启动加载器
        ServiceLoader<ApplicationLoader> applicationLoaders = ServiceLoader.load(ApplicationLoader.class);
        for (ApplicationLoader applicationLoader : applicationLoaders) {
            loaders.add(applicationLoader);
        }
        // 排序
        loaders.sort(new OrderUtil.OrderAsc());

        // 执行加载器
        for (ApplicationLoader applicationLoader : loaders) {
            Class<? extends ApplicationLoader> cls = applicationLoader.getClass();

            // 获取配置属性前缀
            String propertiesPrefix = getPropertiesPrefix(cls);
            // 配置属性填充
            appArguments.populate(propertiesPrefix, applicationLoader);

            // 方法Bean注入
            for (Method method : cls.getDeclaredMethods()) {
                if (conditionBeanMatch(method, this, appArguments)) {
                    BeanDefinition beanDefinition = new MethodBeanDefinition(applicationLoader, method);
                    registerBeanDefinition(beanDefinition.getBeanName(), beanDefinition);
                }
            }
            // 执行加载方法
            applicationLoader.load(this, appArguments);
        }
    }

    public String getPropertiesPrefix(Class<? extends ApplicationLoader> cls) {
        ConfigurationProperties annotation = cls.getAnnotation(ConfigurationProperties.class);
        if (annotation != null) {
            return annotation.prefix() + ".";
        }
        return "";
    }

    /**
     * 判断一个方法Bean是否注入到IOC中
     *
     * @param method             方法
     * @param applicationContext 应用程序上下文
     * @return boolean
     */
    public boolean conditionBeanMatch(Method method, ApplicationContext applicationContext, AppArguments appArguments) {
        // 如果没有@Bean注解，不注入
        if (!method.isAnnotationPresent(Bean.class)) {
            return false;
        }
        Conditional conditional = method.getAnnotation(Conditional.class);
        if (conditional != null) {
            // 有条件，条件通过注入
            Condition condition = ReflectUtil.newInstance(conditional.value());
            return condition.matchers(applicationContext, appArguments);
        }
        // 如果没有条件注解，直接注入
        return true;
    }

}
