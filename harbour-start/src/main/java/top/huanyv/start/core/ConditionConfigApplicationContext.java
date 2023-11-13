package top.huanyv.start.core;

import top.huanyv.bean.annotation.Bean;
import top.huanyv.bean.ioc.AbstractApplicationContext;
import top.huanyv.bean.ioc.AnnotationBeanDefinitionReader;
import top.huanyv.bean.ioc.ApplicationContext;
import top.huanyv.bean.ioc.Configuration;
import top.huanyv.bean.ioc.definition.BeanDefinition;
import top.huanyv.bean.ioc.definition.MethodBeanDefinition;
import top.huanyv.start.anntation.Conditional;
import top.huanyv.start.anntation.Properties;
import top.huanyv.start.loader.ApplicationLoader;
import top.huanyv.start.loader.Condition;
import top.huanyv.bean.utils.NumberUtil;
import top.huanyv.bean.utils.ReflectUtil;
import top.huanyv.bean.utils.StringUtil;

import java.lang.reflect.Field;
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
        reader = new AnnotationBeanDefinitionReader(this.beanDefinitionRegistry);
        reader.read(scanPackages);
    }

    @Override
    public void onRefresh() {
        callLoaders();
        // 加载方法和工厂BeanDefinition
        refreshBeanDefinition();
        // 执行BeanDefinition后置处理器
        invokeBeanDefinitionRegistryPostProcessor();
        // 重新 加载方法和工厂BeanDefinition
        refreshBeanDefinition();
    }

    /**
     * 调用加载器，并加载所有条件Bean
     */
    public void callLoaders() {
        List<ApplicationLoader> loaders = new ArrayList<>();
        // 获取SPI，应用启动加载器
        ServiceLoader<ApplicationLoader> applicationLoaders = ServiceLoader.load(ApplicationLoader.class);
        for (ApplicationLoader applicationLoader : applicationLoaders) {
            loaders.add(applicationLoader);
        }
        // 排序
        loaders.sort((o1, o2) -> o1.getOrder() - o2.getOrder());

        // 执行加载器
        for (ApplicationLoader applicationLoader : loaders) {
            Class<? extends ApplicationLoader> cls = applicationLoader.getClass();

            // 填充属性
            populateLoader(applicationLoader, this.getConfiguration());

            // 执行加载方法
            applicationLoader.load(this, this.getConfiguration());

            // 方法Bean注入
            for (Method method : cls.getDeclaredMethods()) {
                if (conditionBeanMatch(method, this, this.getConfiguration())) {
                    BeanDefinition beanDefinition = new MethodBeanDefinition(() -> applicationLoader, method);
                    registerBeanDefinition(beanDefinition.getBeanName(), beanDefinition);
                }
            }
        }
    }

    public void populateLoader(ApplicationLoader loader, Configuration configuration) {
        Class<? extends ApplicationLoader> cls = loader.getClass();
        String basePrefix = "";
        // 加载类上的配置
        Properties clsAnnotation = cls.getAnnotation(Properties.class);
        if (clsAnnotation != null) {
            basePrefix = clsAnnotation.prefix().trim();
        }
        for (Field field : cls.getDeclaredFields()) {
            field.setAccessible(true);
            String prefixName = basePrefix;
            String configName = field.getName();
            // 属性单独配置
            Properties fieldAnnotation = field.getAnnotation(Properties.class);
            if (fieldAnnotation != null) {
                prefixName = fieldAnnotation.prefix().trim();
                if (StringUtil.hasText(fieldAnnotation.name())) {
                    configName = fieldAnnotation.name().trim();
                }
            }
            String strVal = configuration.get(prefixName + configName);
            if (String.class.equals(field.getType()) && strVal != null) {
                ReflectUtil.setField(field, loader, strVal);
            } else if (StringUtil.hasText(strVal)) {
                Object val = NumberUtil.parse(field.getType(), strVal);
                if (val != null) {
                    ReflectUtil.setField(field, loader, val);
                }
            }
        }
    }

    /**
     * 判断一个方法Bean是否注入到IOC中
     *
     * @param method             方法
     * @param applicationContext 应用程序上下文
     * @return boolean
     */
    public boolean conditionBeanMatch(Method method, ApplicationContext applicationContext, Configuration configuration) {
        // 如果没有@Bean注解，不注入
        if (!method.isAnnotationPresent(Bean.class)) {
            return false;
        }
        Conditional conditional = method.getAnnotation(Conditional.class);
        if (conditional != null) {
            // 有条件，条件通过注入
            Condition condition = ReflectUtil.newInstance(conditional.value());
            return condition.matchers(applicationContext, configuration);
        }
        // 如果没有条件注解，直接注入
        return true;
    }

}
