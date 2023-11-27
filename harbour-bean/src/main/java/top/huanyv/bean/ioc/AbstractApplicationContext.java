package top.huanyv.bean.ioc;

import top.huanyv.bean.annotation.Bean;
import top.huanyv.bean.annotation.Inject;
import top.huanyv.bean.annotation.Value;
import top.huanyv.bean.aop.AopContext;
import top.huanyv.bean.aop.ProxyFactory;
import top.huanyv.bean.exception.BeanCurrentlyInCreationException;
import top.huanyv.bean.exception.NoSuchBeanDefinitionException;
import top.huanyv.bean.ioc.definition.BeanDefinition;
import top.huanyv.bean.ioc.definition.ClassBeanDefinition;
import top.huanyv.bean.ioc.definition.FactoryBeanDefinition;
import top.huanyv.bean.ioc.definition.MethodBeanDefinition;
import top.huanyv.bean.utils.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractApplicationContext implements ApplicationContext {

    // AOP上下文
    private final AopContext aopContext;

    // BeanDefinition注册器
    protected final BeanDefinitionRegistry beanDefinitionRegistry;

    // 一级缓存，单例池
    private final Map<String, Object> singletonObjects;

    // 二级缓存，未注入属性的Bean
    private final Map<String, Object> earlySingletonObjects;

    // 三级缓存，对象工厂，加工后的Bean
    private final Map<String, ObjectFactory<?>> objectsFactories;

    // 当前正在加载的 Bean ，避免循环依赖
    private final Set<String> currentBeans;

    // 配置类组合
    private final ConfigurationComposite configuration;

    // bean构建后置处理器，工厂集合
    private final List<BeanPostProcessor> beanPostProcessorList;

    public AbstractApplicationContext() {
        aopContext = new AopContext();
        beanDefinitionRegistry = new BeanDefinitionRegistry();
        singletonObjects = new ConcurrentHashMap<>();
        earlySingletonObjects = new ConcurrentHashMap<>();
        objectsFactories = new ConcurrentHashMap<>();
        currentBeans = new ConcurrentHashSet<>();
        configuration = new ConfigurationComposite();
        beanPostProcessorList = new ArrayList<>();
    }

    @Override
    public void refresh() {
        // 加载配置类配置
        initConfig();

        // 加载方法和工厂BeanDefinition
        refreshBeanDefinition();
        // 执行BeanDefinition后置处理器
        invokeBeanDefinitionRegistryPostProcessor();
        // 重新 加载方法和工厂BeanDefinition
        refreshBeanDefinition();

        // 空方法，由子类实现，回调
        onRefresh();

        // 组装Bean后置处理器集合
        loadBeanPostProcessor();

        // 执行应用上下文回调
        invokeAwareCallback();
        // 加载非懒Bean
        finishRefresh();
    }

    protected void onRefresh() {

    }


    /**
     * 初始化配置类配置参数
     */
    protected void initConfig() {
        // 配置类配置
        for (String beanName : beanDefinitionRegistry.getBeanDefinitionNames()) {
            BeanDefinition beanDefinition = beanDefinitionRegistry.getBeanDefinition(beanName);
            if (Configuration.class.isAssignableFrom(beanDefinition.getBeanClass())) {
                this.configuration.put((Configuration) beanDefinition.newInstance());
            }
        }
    }

    /**
     * 执行BeanDefinitionRegistry后置处理器
     */
    protected void invokeBeanDefinitionRegistryPostProcessor() {
        // BeanDefinition后置处理器
        for (String beanName : beanDefinitionRegistry.getBeanDefinitionNames()) {
            BeanDefinition beanDefinition = beanDefinitionRegistry.getBeanDefinition(beanName);
            if (BeanDefinitionRegistryPostProcessor.class.isAssignableFrom(beanDefinition.getBeanClass())) {
                BeanDefinitionRegistryPostProcessor postProcessor = getBean(beanName, BeanDefinitionRegistryPostProcessor.class);
                postProcessor.postProcessBeanDefinitionRegistry(this.beanDefinitionRegistry);
            }
        }
    }

    /**
     * 注入方法Bean和FactoryBean
     */
    protected void refreshBeanDefinition() {
        // 配置类注入方法Bean
        for (String beanName : beanDefinitionRegistry.getBeanDefinitionNames()) {
            BeanDefinition beanDefinition = beanDefinitionRegistry.getBeanDefinition(beanName);
            if (Configuration.class.isAssignableFrom(beanDefinition.getBeanClass())) {
                ObjectFactory<Object> objectFactory = () -> getBean(beanName);
                for (Method method : beanDefinition.getBeanClass().getDeclaredMethods()) {
                    Bean methodBean = method.getAnnotation(Bean.class);
                    if (method.isAnnotationPresent(Bean.class)) {
                        BeanDefinition methodBeanDefinition = new MethodBeanDefinition(objectFactory, method);
                        this.beanDefinitionRegistry.register(methodBean.value(), methodBeanDefinition);
                    }
                }
            }
        }
        // 加载FactoryBean
        for (String beanName : beanDefinitionRegistry.getBeanDefinitionNames()) {
            BeanDefinition beanDefinition = beanDefinitionRegistry.getBeanDefinition(beanName);
            if (FactoryBean.class.isAssignableFrom(beanDefinition.getBeanClass()) && beanName.startsWith("&")) {
                BeanDefinition factoryBeanDefinition = new FactoryBeanDefinition(
                        () -> (FactoryBean<?>) getBean(beanName),
                        beanDefinition.getBeanClass(),
                        (FactoryBean<?>) beanDefinition.newInstance()
                );
                this.beanDefinitionRegistry.register(beanName.substring(1), factoryBeanDefinition);
            }
        }
    }

    /**
     * 组装Bean后置处理器
     */
    protected void loadBeanPostProcessor() {
        for (String beanName : beanDefinitionRegistry.getBeanDefinitionNames()) {
            BeanDefinition beanDefinition = beanDefinitionRegistry.getBeanDefinition(beanName);
            if (BeanPostProcessor.class.isAssignableFrom(beanDefinition.getBeanClass())) {
                this.beanPostProcessorList.add((BeanPostProcessor) getBean(beanName));
            }
        }
    }

    /**
     * 执行ApplicationContextAware回调
     */
    protected void invokeAwareCallback() {
        for (String beanName : beanDefinitionRegistry.getBeanDefinitionNames()) {
            BeanDefinition beanDefinition = beanDefinitionRegistry.getBeanDefinition(beanName);
            if (ApplicationContextAware.class.isAssignableFrom(beanDefinition.getBeanClass())) {
                ApplicationContextAware aware = getBean(beanName, ApplicationContextAware.class);
                aware.setApplicationContext(this);
            }
        }
    }

    /**
     * 实例所有的非Lazy的单例Bean
     */
    protected void finishRefresh() {
        for (String beanName : beanDefinitionRegistry.getBeanDefinitionNames()) {
            BeanDefinition beanDefinition = beanDefinitionRegistry.getBeanDefinition(beanName);
            if (!beanDefinition.isLazy()) {
                getBean(beanName);
            }
        }
    }

    /**
     * 给Bean属性注入，依赖注入和配置注入
     *
     * @param bean Bean实例
     */
    private void populateBean(Object bean) {
        Class<?> beanClass = bean.getClass();
        // 配置注入
        for (Field field : beanClass.getDeclaredFields()) {
            field.setAccessible(true);
            Value valueAnnotation = field.getAnnotation(Value.class);
            if (valueAnnotation != null) {
                String key = getConfiguration().get(valueAnnotation.value());
                Object val = NumberUtil.parse(field.getType(), key);
                if (val != null) {
                    ReflectUtil.setField(field, bean, val);
                }
            }
        }
        // 属性注入
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
                if (val != null) {
                    ReflectUtil.setField(field, bean, val);
                }
            }
        }
    }

    @Override
    public void register(Class<?>... componentClasses) {
        if (componentClasses == null) {
            return;
        }
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
        BeanDefinition beanDefinition = new ClassBeanDefinition(beanClass, constructorArgs);
        this.registerBeanDefinition(beanName, beanDefinition);
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

    /**
     * 获取Bean
     *
     * @param beanName bean名字
     * @return {@link Object}
     * @throws RuntimeException 运行时异常
     */
    @Override
    public synchronized Object getBean(String beanName) throws RuntimeException {
        Assert.notNull(beanName, "'beanName' must not be null.");
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

    /**
     * 创建新的Bean实例
     *
     * @param beanName       bean名字
     * @param beanDefinition bean定义
     * @return {@link Object}
     */
    private Object createBean(String beanName, BeanDefinition beanDefinition) throws RuntimeException {
        Object bean = beanDefinition.newInstance();

        if (beanDefinition.isSingleton()) {
            // 放入三级缓存
            addSingletonFactory(beanName, () -> getBeanProxy(beanDefinition, bean));
        }

        this.currentBeans.add(beanName);

        // 填充属性
        populateBean(bean);

        this.currentBeans.remove(beanName);

        Object earlyBean = bean;
        if (beanDefinition.isSingleton()) {
            // 从三缓获取对象工厂，获取代理对象，并放入二缓、从三缓删除
            earlyBean = getSingleton(beanName);
            if (earlyBean == null) {
                earlyBean = bean;
            }
        }

        // 初始化前置处理
        for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
            earlyBean = beanPostProcessor.postProcessBeforeInitialization(earlyBean, beanName);
        }
        // 属性注入后，初始化Bean
        if (earlyBean instanceof InitializingBean) {
            ((InitializingBean) earlyBean).afterPropertiesSet();
        }
        // 初始化后置处理
        for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
            earlyBean = beanPostProcessor.postProcessAfterInitialization(earlyBean, beanName);
        }

        if (beanDefinition.isSingleton()) {
            // 放入单例池一缓，并从二缓删除
            registerSingleton(beanName, earlyBean);
        }
        return earlyBean;
    }

    /**
     * 获取单例，依次从缓存中找，如果从三级缓存中找到返回Bean，并放入二级缓存中
     *
     * @param beanName bean名字
     * @return {@link Object}
     */
    private Object getSingleton(String beanName) {
        // 从一级缓存中找
        Object bean = this.singletonObjects.get(beanName);
        if (bean == null) {
            // 一级缓存没有，从二级缓存找
            bean = this.earlySingletonObjects.get(beanName);
            if (bean == null) {
                // 二级缓存没有，从三级缓存获取ObjectFactory对象工厂
                ObjectFactory<?> objectFactory = this.objectsFactories.get(beanName);
                if (objectFactory != null) {
                    // 得到对象工厂
                    bean = objectFactory.getObject();
                    // 加工后的Bean放入二级缓存，从三级缓存删除掉
                    this.earlySingletonObjects.put(beanName, bean);
                    objectsFactories.remove(beanName);
                }
            }
        }
        return bean;
    }

    /**
     * Bean放入单例池
     *
     * @param beanName     bean名字
     * @param beanInstance bean实例
     */
    private void registerSingleton(String beanName, Object beanInstance) {
        synchronized (this.singletonObjects) {
            this.singletonObjects.put(beanName, beanInstance);
            this.earlySingletonObjects.remove(beanName);
            this.objectsFactories.remove(beanName);
        }
    }

    /**
     * 添加对象工厂（三级缓存）
     *
     * @param beanName      bean名字
     * @param objectFactory 对象工厂
     */
    private void addSingletonFactory(String beanName, ObjectFactory<?> objectFactory) {
        if (!this.singletonObjects.containsKey(beanName)) {
            this.objectsFactories.put(beanName, objectFactory);
            this.earlySingletonObjects.remove(beanName);
        }
    }

    /**
     * 得到bean代理，如果不需要代理返回的是原对象
     *
     * @param beanDefinition bean定义
     * @param bean           豆
     * @return {@link Object}
     */
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
    public boolean containsBean(String beanName) {
        Assert.notNull(beanName, "'beanName' must not be null.");
        return beanDefinitionRegistry.containsBeanDefinition(beanName);
    }

    @Override
    public Configuration getConfiguration() {
        return this.configuration;
    }
}

