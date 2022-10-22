package top.huanyv.ioc.core;


import top.huanyv.ioc.anno.*;
import top.huanyv.ioc.aop.AopContext;
import top.huanyv.ioc.aop.ProxyFactory;
import top.huanyv.ioc.exception.BeanTypeNonUniqueException;
import top.huanyv.utils.ClassUtil;
import top.huanyv.utils.ReflectUtil;
import top.huanyv.utils.StringUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class AnnotationConfigApplicationContext implements ApplicationContext {

    private AopContext aopContext = new AopContext();

    private BeanDefinitionRegistry beanDefinitionRegistry = new BeanDefinitionRegistry();

    // 一级缓存，bean容器
    private Map<String, Object> beanPool = new ConcurrentHashMap<>();

    // 二级缓存，创建的bean实例放到这个map中，然后再判断代理不代理放到一级缓存中
    private Map<String, Object> earlyBeans = new ConcurrentHashMap<>();

    public AnnotationConfigApplicationContext(String... scanPackages) {

        // 扫描配置类，注入方法Bean
        findConfiguration(scanPackages);

        List<ApplicationContextWeave> weaves = getWeave();

        for (ApplicationContextWeave weave : weaves) {
            weave.findConfigurationAfter(this);
        }

        // 创建所有的组件Bean
        createComponent(scanPackages);

        for (ApplicationContextWeave weave : weaves) {
            weave.createComponentAfter(this);
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
     * 配置bean
     *
     * @param basePack 扫描包
     */
    private void findConfiguration(String... basePack) {
        Set<Class<?>> classes = ClassUtil.getClassesByAnnotation(Configuration.class, basePack);
        // 遍历配置类
        for (Class<?> clazz : classes) {
            try {
                // 创建配置类实例，要执行里面的方法获取bean实例
                Object configBean = clazz.getConstructor().newInstance();
                // 遍历类中的方法，哪个方法有@Bean注解
                Method[] methods = clazz.getDeclaredMethods();
                for (Method method : methods) {
                    method.setAccessible(true);
                    if (method.isAnnotationPresent(Bean.class)) {
                        String beanName = method.getName();
                        Object beanInstance = method.invoke(configBean);
                        this.earlyBeans.put(beanName, beanInstance);

                        this.beanDefinitionRegistry.register(beanName, new BeanDefinition(beanName, beanInstance.getClass()));
                    }
                }
            } catch (InstantiationException | IllegalAccessException
                    | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
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
     * 找到所有的Bean定义
     *
     * @param basePack 基本包
     */
    private void createComponent(String... basePack) {
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
//                aopContext.add(clazz);
                // 创建组件实例
                Object instance = ReflectUtil.newInstance(clazz);
                this.earlyBeans.put(beanName, instance);
            }
        }
    }

    /**
     * 代理bean
     */
    private void proxyBean() {
        for (Map.Entry<String, Object> beanEntry : this.earlyBeans.entrySet()) {
            String beanName = beanEntry.getKey();
            Object beanInstance = beanEntry.getValue();
            Class<?> beanClass = beanInstance.getClass();
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

    /**
     * 注入Bean
     */
    private void populateBean() {
        // 给bean原实例填充
        for (Map.Entry<String, Object> beanEntry : this.earlyBeans.entrySet()) {
            Object beanInstance = beanEntry.getValue();
            Class<?> beanClass = beanInstance.getClass();
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
                        field.set(beanInstance, val);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void registerBean(Object bean) {
        String beanName = StringUtil.firstLetterLower(bean.getClass().getSimpleName());
        registerBean(beanName, bean);
    }

    @Override
    public void registerBean(String beanName, Object beanInstance) {
        this.earlyBeans.put(beanName, beanInstance);
        Class<?> beanClass = beanInstance.getClass();
        beanDefinitionRegistry.register(beanName, new BeanDefinition(beanName, beanClass));
        // 是否需要代理
        if (AopContext.isNeedProxy(beanClass)) {
            this.beanPool.put(beanName, ProxyFactory.getProxy(beanInstance, aopContext));
        } else {
            this.beanPool.put(beanName, beanInstance);
        }
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
    public <T> T getBean(Class<T> type) throws BeanTypeNonUniqueException {
        T result = null;
        int count = 0;
        for (Map.Entry<String, Object> entry : this.beanPool.entrySet()) {
            Object beanInstance = entry.getValue();
            if (type.isInstance(beanInstance)) {
                count++;
                if (count > 1) {
                    throw new BeanTypeNonUniqueException(type);
                }
                result = (T) beanInstance;
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

