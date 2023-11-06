package top.huanyv.bean.ioc;

import top.huanyv.bean.annotation.Bean;
import top.huanyv.bean.annotation.Component;
import top.huanyv.bean.annotation.Configuration;
import top.huanyv.bean.ioc.definition.BeanDefinition;
import top.huanyv.bean.ioc.definition.ClassBeanDefinition;
import top.huanyv.bean.ioc.definition.MethodBeanDefinition;
import top.huanyv.bean.utils.ClassUtil;
import top.huanyv.bean.utils.ReflectUtil;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * 注解解析BeanDefinition
 *
 * @author huanyv
 * @date 2022/12/22 16:22
 */
public class AnnotationBeanDefinitionReader {

    private BeanDefinitionRegistry registry;

    public AnnotationBeanDefinitionReader(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    public void read(String... scanPackages) {
        // 加载组件 bean 定义
        Set<Class<?>> classes = ClassUtil.getClasses(scanPackages);
        for (Class<?> cls : classes) {
            // 加载组件 bean 定义
            Component component = cls.getAnnotation(Component.class);
            if (component != null) {
                BeanDefinition beanDefinition = new ClassBeanDefinition(cls);
                this.registry.register(component.value(), beanDefinition);
            }

            // 加载方法Bean
            if (cls.isAnnotationPresent(Configuration.class)) {
                Object configInstance = ReflectUtil.newInstance(cls);
                for (Method method : cls.getDeclaredMethods()) {
                    method.setAccessible(true);
                    if (method.isAnnotationPresent(Bean.class)) {
                        BeanDefinition beanDefinition = new MethodBeanDefinition(configInstance, method);
                        // 注册
                        this.registry.register(method.getName(), beanDefinition);
                    }
                }
            }
        }
    }


}
