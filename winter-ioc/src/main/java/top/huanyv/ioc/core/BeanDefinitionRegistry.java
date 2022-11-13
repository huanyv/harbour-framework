package top.huanyv.ioc.core;

import top.huanyv.ioc.anno.Bean;
import top.huanyv.ioc.anno.Component;
import top.huanyv.ioc.anno.Configuration;
import top.huanyv.ioc.core.definition.BeanDefinition;
import top.huanyv.ioc.core.definition.ClassBeanDefinition;
import top.huanyv.ioc.core.definition.FactoryBeanDefinition;
import top.huanyv.ioc.core.definition.MethodBeanDefinition;
import top.huanyv.ioc.exception.BeanTypeNonUniqueException;
import top.huanyv.tools.utils.ClassUtil;
import top.huanyv.tools.utils.ReflectUtil;
import top.huanyv.tools.utils.StringUtil;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author huanyv
 * @date 2022/8/8 14:50
 */
public class BeanDefinitionRegistry implements Iterable<BeanDefinition>{

    private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    public void loadBeanDefinition(String... scanPackages) {
        // 加载组件 bean 定义
        Set<Class<?>> classes = ClassUtil.getClasses(scanPackages);
        for (Class<?> cls : classes) {
            // 加载组件 bean 定义
            Component component = cls.getAnnotation(Component.class);
            if (component != null) {
                register(component.value(), cls);
            }

            // 加载方法Bean
            if (cls.isAnnotationPresent(Configuration.class)) {
                Object configInstance = ReflectUtil.newInstance(cls);
                for (Method method : cls.getDeclaredMethods()) {
                    method.setAccessible(true);
                    if (method.isAnnotationPresent(Bean.class)) {
                        BeanDefinition beanDefinition = new MethodBeanDefinition(configInstance, method);
                        this.beanDefinitionMap.put(method.getName(), beanDefinition);
                    }
                }
            }
        }

    }

    public void register(String beanName, Class<?> cls, Object... constructorArgs) {
        // 组件Bean
        ClassBeanDefinition classBeanDefinition = new ClassBeanDefinition(cls, constructorArgs);
        beanName = StringUtil.hasText(beanName) ? beanName : classBeanDefinition.getBeanName();
        if (FactoryBean.class.isAssignableFrom(cls)) {
            // 工厂Bean注册
            BeanDefinition factoryBeanDefinition = new FactoryBeanDefinition((FactoryBean<?>) classBeanDefinition.newInstance());
            this.beanDefinitionMap.put(beanName, factoryBeanDefinition);

            beanName = "&" + beanName;
        }
        this.beanDefinitionMap.put(beanName, classBeanDefinition);
    }

    public void register(BeanDefinition beanDefinition) {
        beanDefinitionMap.put(beanDefinition.getBeanName(), beanDefinition);
    }

    public void register(String beanName, BeanDefinition beanDefinition) {
        beanDefinitionMap.put(beanName, beanDefinition);
    }

    /**
     * 得到bean定义
     *
     * @param beanName bean名字
     * @return {@link BeanDefinition}
     */
    public BeanDefinition getBeanDefinition(String beanName) {
        return this.beanDefinitionMap.get(beanName);
    }

    /**
     * 得到bean定义Map
     *
     * @return {@link Map}<{@link String}, {@link BeanDefinition}>
     */
    public Map<String, BeanDefinition> getBeanDefinitionMap() {
        return beanDefinitionMap;
    }

    /**
     * 获得bean定义名称
     *
     * @return {@link String[]}
     */
    public String[] getBeanDefinitionNames() {
        return this.beanDefinitionMap.keySet().toArray(new String[0]);
    }

    public String getBeanName(Class<?> type) {
        String beanName = null;
        int count = 0;
        for (Map.Entry<String, BeanDefinition> entry : this.beanDefinitionMap.entrySet()) {
            BeanDefinition beanDefinition = entry.getValue();
            if (type.isAssignableFrom(beanDefinition.getBeanClass())) {
                count++;
                if (count > 1) {
                    throw new BeanTypeNonUniqueException(type);
                }
                beanName = entry.getKey();
            }
        }
        return beanName;
    }

    public boolean containsBeanDefinition(String beanName) {
        return this.beanDefinitionMap.containsKey(beanName);
    }

    @Override
    public Iterator<BeanDefinition> iterator() {
        return this.beanDefinitionMap.values().iterator();
    }

}
