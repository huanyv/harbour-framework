package top.huanyv.ioc.core;


import top.huanyv.ioc.anno.*;
import top.huanyv.ioc.aop.ProxyFactory;
import top.huanyv.ioc.exception.BeanTypeNonUniqueException;
import top.huanyv.utils.ClassUtil;
import top.huanyv.utils.StringUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class AnnotationConfigApplicationContext implements ApplicationContext {

    private BeanDefinitionRegistry beanDefinitionRegistry = new BeanDefinitionRegistry();

    // 一级缓存，bean容器
    private Map<String, Object> beanPool = new ConcurrentHashMap<>();

    // 二级缓存，创建的bean实例放到这个map中，然后再判断代理不代理放到一级缓存中
    private Map<String, Object> earlyBeans = new ConcurrentHashMap<>();

    public AnnotationConfigApplicationContext(String... scanPackages) {

        // 1、找到所有的原材料
        findBeanDefinitions(scanPackages);
        // 2、根据原材料创建bean实例,放到二级缓存中
        createBean();
        // 3、代理对要的bean代理
        proxyBean();

        // 配置bean
        configBean(scanPackages);

        // 外部注入的bean
        extendBean();

        // 4、bean注入属性
        autowiredBean();
    }


    private void findBeanDefinitions(String... basePack) {
        //获取basePack包下所有的class类
        Set<Class<?>> classes = ClassUtil.getClasses(basePack);
        for (Class<?> clazz : classes) {
            Component component = clazz.getAnnotation(Component.class);
            if (component != null) {
                String beanName = component.value();
                if (!StringUtil.hasText(beanName)) {
                    beanName = StringUtil.firstLetterLower(clazz.getSimpleName());
                }
                beanDefinitionRegistry.register(beanName, new BeanDefinition(beanName, clazz));
            }
        }
    }

    private void createBean() {
        for (String beanDefinitionName : beanDefinitionRegistry.getBeanDefinitionNames()) {
            BeanDefinition beanDefinition = beanDefinitionRegistry.getBeanDefinition(beanDefinitionName);
            try {
                Constructor<?> constructor = beanDefinition.getBeanClass().getConstructor();
                constructor.setAccessible(true);
                Object instance = constructor.newInstance();
                this.earlyBeans.put(beanDefinition.getBeanName(), instance);
            } catch (NoSuchMethodException | IllegalAccessException
                    | InstantiationException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    private void proxyBean() {
        for (String beanDefinitionName : beanDefinitionRegistry.getBeanDefinitionNames()) {
            BeanDefinition beanDefinition = beanDefinitionRegistry.getBeanDefinition(beanDefinitionName);
            String beanName = beanDefinition.getBeanName();
            Object bean = this.earlyBeans.get(beanName);
            if (beanDefinition.isNeedProxy()) {
                Object proxy = ProxyFactory.getProxy(bean, beanDefinitionRegistry);
                this.beanPool.put(beanName, proxy);
            } else {
                this.beanPool.put(beanName, bean);
            }
        }
    }

    private void configBean(String... basePack) {
        Set<Class<?>> classes = ClassUtil.getClassesByAnnotation(Configuration.class, basePack);
        // 遍历配置类
        for (Class<?> clazz : classes) {
            try {
                // 创建配置类实例，要执行里面的方法获取bean实例
                Object configBean = clazz.getConstructor().newInstance();
                // 遍历类中的方法，哪个方法有@Bean注解
                Method[] methods = clazz.getDeclaredMethods();
                for (Method method : methods) {
                    if (method.isAnnotationPresent(Bean.class)) {
                        String beanName = method.getName();
                        Object beanInstance = method.invoke(configBean);
                        this.beanPool.put(beanName, beanInstance);
                    }
                }
            } catch (InstantiationException | IllegalAccessException
                    | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    private void extendBean() {
        for (Map.Entry<String, Object> beanEntry : this.beanPool.entrySet()) {
            Object bean = beanEntry.getValue();
            if (bean instanceof BeanRegistry) {
                BeanRegistry beanRegistry = (BeanRegistry) bean;
                beanRegistry.set(this);
            }
        }
    }

    private void autowiredBean() {
        for (String beanDefinitionName : beanDefinitionRegistry.getBeanDefinitionNames()) {
            BeanDefinition beanDefinition = beanDefinitionRegistry.getBeanDefinition(beanDefinitionName);
            String beanName = beanDefinition.getBeanName();
            Class<?> beanClass = beanDefinition.getBeanClass();
            Object instance = this.earlyBeans.get(beanName);
            for (Field field : beanClass.getDeclaredFields()) {
                Inject inject = field.getAnnotation(Inject.class);
                Object val = null;
                if (inject != null) {
                    String injectName = inject.value();
                    if (StringUtil.hasText(injectName)) {
                        // 名称注入
                        val = getBean(injectName);
                    } else {
                        val = getBean(field.getType());
                    }
                    field.setAccessible(true);
                    try {
                        field.set(instance, val);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    @Override
    public void register(Object o) {
        String beanName = StringUtil.firstLetterLower(o.getClass().getSimpleName());
        register(beanName, o);
    }

    @Override
    public void register(String beanName, Object o) {
        this.beanPool.put(beanName, o);
    }


    @Override
    public String[] getBeanDefinitionNames() {
        return this.beanDefinitionRegistry.getBeanDefinitionNames();
    }

    @Override
    public int getBeanDefinitionCount() {
        return this.beanDefinitionRegistry.getBeanDefinitionMap().size();
    }

    @Override
    public BeanDefinition getBeanDefinition(String beanName) {
        return this.beanDefinitionRegistry.getBeanDefinition(beanName);
    }

    @Override
    public Object getBean(String beanName) {
        return this.beanPool.get(beanName);
    }

    @Override
    public <T> T getBean(Class<T> clazz) {
        T result = null;
        int count = 0;
        for (Map.Entry<String, Object> entry : this.beanPool.entrySet()) {
            String beanName = entry.getKey();
            Object instance = entry.getValue();
            if (clazz.isInstance(instance)) {
                count++;
                if (count > 1) {
                    throw new BeanTypeNonUniqueException(clazz);
                }
                result = (T) instance;
            }
        }
        return result;
    }

    @Override
    public <T> T getBean(String beanName, Class<T> type) {
        return (T) getBean(beanName);
    }

    @Override
    public boolean containsBean(String name) {
        return this.beanPool.containsKey(name);
    }
}

