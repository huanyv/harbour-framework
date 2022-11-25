package top.huanyv.bean.ioc;

import top.huanyv.bean.annotation.Configuration;
import top.huanyv.bean.ioc.definition.BeanDefinition;
import top.huanyv.bean.annotation.Bean;
import top.huanyv.bean.annotation.Component;
import top.huanyv.bean.ioc.definition.ClassBeanDefinition;
import top.huanyv.bean.ioc.definition.FactoryBeanDefinition;
import top.huanyv.bean.ioc.definition.MethodBeanDefinition;
import top.huanyv.bean.exception.BeanTypeNonUniqueException;
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
                        // 注册
                        register(method.getName(), beanDefinition);
                    }
                }
            }
        }

    }

    /**
     * 注册一个@Component注解声明的Bean
     *
     * @param beanName        bean名字
     * @param cls             cls
     * @param constructorArgs 构造函数参数
     */
    public void register(String beanName, Class<?> cls, Object... constructorArgs) {
        // 组件Bean
        ClassBeanDefinition classBeanDefinition = new ClassBeanDefinition(cls, constructorArgs);
        this.register(beanName, classBeanDefinition);
    }

    /**
     * 注册一个BeanDefinition
     *
     * @param beanDefinition bean定义
     */
    public void register(BeanDefinition beanDefinition) {
        register(beanDefinition.getBeanName(), beanDefinition);
    }

    /**
     * 注册一个{@link BeanDefinition}，指定BeanName <br />
     * 如果是{@link FactoryBean}，{@link FactoryBean#getObject()}获得的Bean与BeanName成对，
     * FactoryBean实例将把BeanName加上'&'符号,放入容器中
     *
     * @param beanName       bean名字
     * @param beanDefinition bean定义
     */
    public void register(String beanName, BeanDefinition beanDefinition) {
        Class<?> cls = beanDefinition.getBeanClass();
        // 判断BeanNe是否可用
        beanName = StringUtil.hasText(beanName) ? beanName : beanDefinition.getBeanName();
        // FactoryBean
        if (FactoryBean.class.isAssignableFrom(cls)) {
            // 工厂Bean注册
            BeanDefinition factoryBeanDefinition = new FactoryBeanDefinition((FactoryBean<?>) beanDefinition.newInstance());
            this.beanDefinitionMap.put(beanName, factoryBeanDefinition);

            beanName = "&" + beanName;
        }
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
