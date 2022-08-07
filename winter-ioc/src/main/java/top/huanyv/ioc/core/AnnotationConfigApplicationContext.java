package top.huanyv.ioc.core;


import top.huanyv.ioc.anno.*;
import top.huanyv.ioc.aop.ProxyFactory;
import top.huanyv.utils.ClassUtil;
import top.huanyv.utils.StringUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class AnnotationConfigApplicationContext implements ApplicationContext {

    /**
     * Bean容器
     */
    private Map<String, Object> beanMap = new ConcurrentHashMap<>();

    private Map<String, Object> sourceBeanMap = new ConcurrentHashMap<>();

    /**
     * bean定义
     */
    private Set<BeanDefinition> beanDefinitions = new HashSet<>();


    public AnnotationConfigApplicationContext(String... basePackages) {
        //遍历包，找到目标类(原材料)
        findBeanDefinitions(basePackages);

        //根据原材料创建bean
        createBean(beanDefinitions);

        // 配置一个bean，有属性值
        for (int i = 0; i < basePackages.length; i++) {
            configBean(basePackages[i]);
        }

        // 外部注入的bean
        Set<BeanRegistry> beanRegistries = extendBean();
        for (BeanRegistry beanRegistry : beanRegistries) {
            beanRegistry.set(this);
        }

        // 自动装载
        autowiredBean(beanDefinitions);

    }

    /**
     * 将class类封装成BeanDefinition
     * 格式：name->beanName  class->class类
     *
     * @param basePack
     * @return BeanDefinition集合
     */
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
                beanDefinitions.add(new BeanDefinition(beanName, clazz));
            }
        }
    }

    /**
     * 实例化对象并填充属性
     * @param beanDefinitions
     */
    private void createBean(Set<BeanDefinition> beanDefinitions) {
        for (BeanDefinition beanDefinition : beanDefinitions) {
            String beanName = beanDefinition.getBeanName();
            Class beanClass = beanDefinition.getBeanClass();
            try {
                Constructor constructor = beanClass.getConstructor();
                constructor.setAccessible(true);
                Object beanInstance = constructor.newInstance();
                // bean map中存放代理类， 从窗口中取出的也是代理类
                Object proxyBean = ProxyFactory.getProxy(beanInstance);
                this.beanMap.put(beanName, proxyBean);

                // 原始类型的map，被代理的类对象，
                this.sourceBeanMap.put(beanName, beanInstance);
            } catch (NoSuchMethodException | IllegalAccessException
                    | InstantiationException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }


    private void configBean(String basePack) {
        Set<Class<?>> classes = ClassUtil.getClassesByAnnotation(basePack, Configuration.class);
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
                        this.beanMap.put(beanName, beanInstance);
                    }
                }
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    private Set<BeanRegistry> extendBean() {
        Set<BeanRegistry> beanRegistries = new HashSet<>();
        for (Map.Entry<String, Object> beanEntry : this.beanMap.entrySet()) {
            Object bean = beanEntry.getValue();
            if (bean instanceof BeanRegistry) {
                BeanRegistry beanRegistry = (BeanRegistry) bean;
                beanRegistries.add(beanRegistry);
            }
        }
        return beanRegistries;
//
//        Set<BeanDefinition> beanDefinitions = this.beanDefinitions.stream()
//                .filter(beanDefinition -> BeanRegistry.class.isAssignableFrom(beanDefinition.getBeanClass()))
//                .collect(Collectors.toSet());
//
//        autowiredBean(beanDefinitions);
//
//        return beanDefinitions.stream()
//                .map(beanDefinition -> (BeanRegistry) getBean(beanDefinition.getBeanName()))
//                .collect(Collectors.toSet());
    }

    /**
     * 自动装配填充，从代理对象map中，注入到被代理的对象中
     * @param beanDefinitions bean定义
     */
    private void autowiredBean(Set<BeanDefinition> beanDefinitions) {
        for (BeanDefinition beanDefinition : beanDefinitions) {
            String beanName = beanDefinition.getBeanName();
            Class beanClass = beanDefinition.getBeanClass();
            // 被代理的类
            Object bean = this.sourceBeanMap.get(beanName);
            // 属性注入
            for (Field field : beanClass.getDeclaredFields()) {
                Object val = null;
                // 注入
                if (field.isAnnotationPresent(Autowired.class)) {
                    // 名字注入
                    if (field.isAnnotationPresent(Qualifier.class)) {
                        val = getBean(field.getName());
                    } else { // 根据类型注入
                        val = getBean(field.getType());
                    }
                    try {
                        field.setAccessible(true);
                        field.set(bean, val);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    /**
     * 根据名称获取bean
     *
     * @param beanName bean名称
     * @return bean实例
     */
    @Override
    public Object getBean(String beanName) {
        return beanMap.get(beanName);
    }

    @Override
    public <T> T getBean(Class<T> clazz) {
        for (Map.Entry<String, Object> entry : this.beanMap.entrySet()) {
            Object o = entry.getValue();
            if (clazz.isInstance(o)) {
                return (T) o;
            }
        }
        return null;
    }

    @Override
    public <T> T getBean(String beanName, Class<T> type) {
        return (T) getBean(beanName);
    }

    @Override
    public boolean containsBean(String name) {
        return this.beanMap.containsKey(name);
    }

    @Override
    public void register(Object o) {
        String beanName = StringUtil.firstLetterLower(o.getClass().getSimpleName());
        register(beanName, o);
    }

    @Override
    public void register(String beanName, Object o) {
        this.beanMap.put(beanName, o);
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return this.beanDefinitions.stream().map(BeanDefinition::getBeanName).distinct().toArray(String[]::new);
    }

    @Override
    public int getBeanDefinitionCount() {
        return this.beanMap.size();
    }

    @Override
    public BeanDefinition getBeanDefinition(String beanName) {
        if (beanName == null) {
            return null;
        }
        for (BeanDefinition beanDefinition : this.beanDefinitions) {
            if (beanName.equals(beanDefinition.getBeanName())) {
                return beanDefinition;
            }
        }
        return null;
    }
}

