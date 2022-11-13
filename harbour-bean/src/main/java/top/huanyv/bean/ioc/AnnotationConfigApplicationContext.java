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

    private AopContext aopContext = new AopContext();

    private BeanDefinitionRegistry beanDefinitionRegistry = new BeanDefinitionRegistry();

    // 一级缓存，bean容器
    private Map<String, Object> beanPool = new ConcurrentHashMap<>();

    // 二级缓存，创建的bean实例放到这个map中，然后再判断代理不代理放到一级缓存中
    private Map<String, Object> earlyBeans = new ConcurrentHashMap<>();

    // 当前正在加载的 Bean ，避免循环依赖
    private Map<String, Object> currentBeans = new ConcurrentHashMap<>();

    public AnnotationConfigApplicationContext(String... scanPackages) {

        // 加载所有的 BeanDefinition
        beanDefinitionRegistry.loadBeanDefinition(scanPackages);

        // 创建 Bean 实例
        createBeanInstance();

        List<ApplicationContextWeave> weaves = getWeave();

        for (ApplicationContextWeave weave : weaves) {
            weave.createBeanInstanceAfter(this);
        }

        // 对需要代理的Bean进行代理
        proxyBean();

        for (ApplicationContextWeave weave : weaves) {
            weave.populateBeanBefore(this);
        }

        // Bean注入属性
        populateBean();

        for (ApplicationContextWeave weave : weaves) {
            weave.populateBeanAfter(this);
        }
    }

    /**
     * 创建bean实例
     */
    private void createBeanInstance() {
        for (String beanDefinitionName : beanDefinitionRegistry.getBeanDefinitionNames()) {
            BeanDefinition beanDefinition = beanDefinitionRegistry.getBeanDefinition(beanDefinitionName);
            // 创建单例实例
            if (BeanDefinition.SCOPE_SINGLETON.equals(beanDefinition.getScope())
                    && !this.earlyBeans.containsKey(beanDefinitionName)) {
                Object beanInstance = beanDefinition.newInstance();
                this.earlyBeans.put(beanDefinitionName, beanInstance);
            }
        }
    }


    /**
     * 获取所有织入类，并实例化，
     *
     * @return {@link List}<{@link ApplicationContextWeave}>
     */
    public List<ApplicationContextWeave> getWeave() {
        List<ApplicationContextWeave> weaveList = new ArrayList<>();

        // SPI 使用JDK的SPI机制
        ServiceLoader<ApplicationContextWeave> serviceLoader = ServiceLoader.load(ApplicationContextWeave.class);
        for (ApplicationContextWeave weave : serviceLoader) {
            weaveList.add(weave);
        }

        // 添加所有的配置织入
        for (Map.Entry<String, Object> entry : this.earlyBeans.entrySet()) {
            Object value = entry.getValue();
            if (value instanceof ApplicationContextWeave) {
                weaveList.add((ApplicationContextWeave) value);
            }
        }

        // 排序
        weaveList.sort((o1, o2) -> o1.getOrder() - o2.getOrder());
        return weaveList;
    }


    /**
     * 代理bean
     */
    private void proxyBean() {
        for (Map.Entry<String, Object> beanEntry : this.earlyBeans.entrySet()) {
            String beanName = beanEntry.getKey();
            Object beanInstance = beanEntry.getValue();
            Class<?> beanClass = beanInstance.getClass();
            if (!this.beanPool.containsKey(beanName)) {
                // 是否需要代理
                if (AopContext.isNeedProxy(beanClass)) {
                    // 代理对象放到 bean 池中
                    this.beanPool.put(beanName, ProxyFactory.getProxy(beanInstance, aopContext));
                    aopContext.add(beanClass);
                } else {
                    // 不需要代理的对象
                    this.beanPool.put(beanName, beanInstance);
                }
            }
        }
    }

    /**
     * 注入Bean
     */
    private void populateBean() {
        // 给bean原实例填充
        for (Map.Entry<String, Object> beanEntry : this.earlyBeans.entrySet()) {
            populateBean(beanEntry.getValue());
        }
    }

    /**
     * 注入一个bean
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
    public void refresh() {
        createBeanInstance();

        proxyBean();

        populateBean();
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
        if (BeanDefinition.SCOPE_PROTOTYPE.equals(beanDefinition.getScope())) {
            // 判断Bean是否在创建中
            if (this.currentBeans.containsKey(beanName)) {
                throw new BeanCurrentlyInCreationException(beanName);
            }
            Object beanInstance = beanDefinition.newInstance();

            this.currentBeans.put(beanName, beanInstance);

            // 对Bean进行填充
            populateBean(beanInstance);

            this.currentBeans.remove(beanName);

            // 是否需要代理
            if (AopContext.isNeedProxy(beanDefinition.getBeanClass())) {
                beanInstance = ProxyFactory.getProxy(beanInstance, aopContext);
                aopContext.add(beanDefinition.getBeanClass());
            }

            return beanInstance;
        }

        // 单例Bean
        return this.beanPool.get(beanName);
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

