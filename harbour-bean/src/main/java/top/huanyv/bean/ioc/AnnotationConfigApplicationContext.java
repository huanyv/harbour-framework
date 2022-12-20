package top.huanyv.bean.ioc;


import top.huanyv.bean.annotation.Inject;
import top.huanyv.bean.aop.ProxyFactory;
import top.huanyv.bean.ioc.definition.BeanDefinition;
import top.huanyv.bean.exception.BeanCurrentlyInCreationException;
import top.huanyv.bean.aop.AopContext;
import top.huanyv.bean.exception.NoSuchBeanDefinitionException;
import top.huanyv.tools.utils.StringUtil;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class AnnotationConfigApplicationContext implements ApplicationContext {

    private final AopContext aopContext;

    private final BeanDefinitionRegistry beanDefinitionRegistry;

    // 一级缓存，单例池
    private final Map<String, Object> singletonObjects;

    // 二级缓存，未注入属性的Bean
    private final Map<String, Object> earlySingletonObjects;

    // 三级缓存，对象工厂，加工后的Bean
    private final Map<String, ObjectFactory> objectsFactories;

    // 当前正在加载的 Bean ，避免循环依赖
    private final Set<String> currentBeans = new HashSet<>();

    public AnnotationConfigApplicationContext(String... scanPackages) {
        aopContext = new AopContext();
        beanDefinitionRegistry = new BeanDefinitionRegistry();
        singletonObjects = new ConcurrentHashMap<>();
        earlySingletonObjects = new ConcurrentHashMap<>();
        objectsFactories = new ConcurrentHashMap<>();

        // 加载所有的 BeanDefinition
        beanDefinitionRegistry.loadBeanDefinition(scanPackages);

        refresh();

    }

    @Override
    public void refresh() {
        initApplicationContextAware();
    }

    public void initApplicationContextAware() {
        for (String beanName : beanDefinitionRegistry.getBeanDefinitionNames()) {
            BeanDefinition beanDefinition = beanDefinitionRegistry.getBeanDefinition(beanName);
            if (ApplicationContextAware.class.isAssignableFrom(beanDefinition.getBeanClass())) {
                ApplicationContextAware aware = (ApplicationContextAware) getBean(beanName);
                aware.setApplicationContext(this);
            }
        }
    }

    /**
     * 注入一个bean
     *
     * @param bean Bean实例
     */
    private void populateBean(Object bean) {
        Class<?> beanClass = bean.getClass();
        for (Field field : beanClass.getDeclaredFields()) {
            field.setAccessible(true);
            Inject inject = field.getAnnotation(Inject.class);
            if (inject != null) {
                String injectName = inject.value();
                Object val = null;
                // 名称注入
                if (StringUtil.hasText(injectName)) {
                    val = getBean(injectName);
                } else {
                    // 类型注入
                    val = getBean(field.getType());
                }
                try {
                    field.set(bean, val);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void register(Class<?>... componentClasses) {
        for (Class<?> cls : componentClasses) {
            registerBean(cls);
        }
    }

    @Override
    public void registerBean(Class<?> beanClass, Object... constructorArgs) {
        this.registerBean(StringUtil.firstLetterLower(beanClass.getSimpleName()), beanClass, constructorArgs);
    }

    @Override
    public void registerBean(String beanName, Class<?> beanClass, Object... constructorArgs) {
        this.beanDefinitionRegistry.register(beanName, beanClass, constructorArgs);
    }

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        this.beanDefinitionRegistry.register(beanName, beanDefinition);
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
    public synchronized Object getBean(String beanName) throws RuntimeException {
        BeanDefinition beanDefinition = beanDefinitionRegistry.getBeanDefinition(beanName);
        if (beanDefinition == null) {
            throw new NoSuchBeanDefinitionException("No bean named '" + beanName + "' available");
        }

        // 多实例Bean
        if (!beanDefinition.isSingleton()) {
            // 判断Bean是否在创建中
            if (this.currentBeans.contains(beanName)) {
                throw new BeanCurrentlyInCreationException(beanName);
            }

            // 创建Bean
            Object beanInstance = createBean(beanName, beanDefinition);

            // 是否需要代理
            beanInstance = getBeanProxy(beanDefinition, beanInstance);

            return beanInstance;
        }

        // 单例bean
        Object singletonBean = getSingleton(beanName);
        if (singletonBean != null) {
            return singletonBean;
        }

        return createBean(beanName, beanDefinition);
    }

    private Object getSingleton(String beanName) {
        // 从一级缓存中找
        Object bean = this.singletonObjects.get(beanName);
        if (bean == null) {
            // 一级缓存没有，从二级缓存找
            bean = this.earlySingletonObjects.get(beanName);
            if (bean == null) {
                // 二级缓存没有，从三级缓存获取 ObjectFactory
                ObjectFactory objectFactory = this.objectsFactories.get(beanName);
                if (objectFactory != null) {
                    // 得去对象工厂
                    bean = objectFactory.getObject();
                    // 加工后的Bean放入二级缓存，三级缓存删除掉
                    this.earlySingletonObjects.put(beanName, bean);
                    objectsFactories.remove(beanName);
                }
            }
        }
        return bean;
    }

    private void registerSingleton(String beanName, Object beanInstance) {
        synchronized (this.singletonObjects) {
            this.singletonObjects.put(beanName, beanInstance);
            this.earlySingletonObjects.remove(beanName);
            this.objectsFactories.remove(beanName);
        }
    }

    private void addSingletonFactory(String beanName, ObjectFactory objectFactory) {
        if (!this.singletonObjects.containsKey(beanName)) {
            this.objectsFactories.put(beanName, objectFactory);
            this.earlySingletonObjects.remove(beanName);
        }
    }

    private Object createBean(String beanName, BeanDefinition beanDefinition) {
        Object bean = beanDefinition.newInstance();

        if (beanDefinition.isSingleton()) {
            // 放入三级缓存
            addSingletonFactory(beanName, () -> getBeanProxy(beanDefinition, bean));
        }

        this.currentBeans.add(beanName);

        // 填充属性
        populateBean(bean);

        this.currentBeans.remove(beanName);

        Object exposedObject = bean;
        if (beanDefinition.isSingleton()) {
            // 获取代理对象
            exposedObject = getSingleton(beanName);
            if (exposedObject == null) {
                exposedObject = bean;
            }

            // 放入单例池
            registerSingleton(beanName, exposedObject);
        }
        return exposedObject;
    }

    private Object getBeanProxy(BeanDefinition beanDefinition, Object bean) {
        Class<?> cls = beanDefinition.getBeanClass();
        if (AopContext.isNeedProxy(cls)) {
            aopContext.add(cls);
            return ProxyFactory.getProxy(bean, aopContext);
        }
        return bean;
    }

    @Override
    public <T> T getBean(Class<T> type) throws NoSuchBeanDefinitionException {
        String beanName = beanDefinitionRegistry.getBeanName(type);
        if (beanName == null) {
            throw new NoSuchBeanDefinitionException("No qualifying bean of type '" + type.getName() + "' available");
        }
        return (T) getBean(beanName);
    }

    @Override
    public <T> T getBean(String beanName, Class<T> type) {
        return (T) getBean(beanName);
    }

    @Override
    public boolean containsBean(String name) {
        return beanDefinitionRegistry.containsBeanDefinition(name);
    }
}

