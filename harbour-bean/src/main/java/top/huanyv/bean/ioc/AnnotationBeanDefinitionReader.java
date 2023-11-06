package top.huanyv.bean.ioc;

import top.huanyv.bean.annotation.Bean;
import top.huanyv.bean.ioc.definition.BeanDefinition;
import top.huanyv.bean.ioc.definition.ClassBeanDefinition;
import top.huanyv.bean.ioc.definition.MethodBeanDefinition;
import top.huanyv.bean.utils.ClassUtil;

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
            Bean classBean = cls.getAnnotation(Bean.class);
            if (classBean != null) {
                BeanDefinition classBeanDefinition = new ClassBeanDefinition(cls);
                this.registry.register(classBean.value(), classBeanDefinition);

                // 加载方法Bean
                if (Configuration.class.isAssignableFrom(cls)) {
                    Object configInstance = classBeanDefinition.newInstance();
                    for (Method method : cls.getDeclaredMethods()) {
                        Bean methodBean = method.getAnnotation(Bean.class);
                        if (method.isAnnotationPresent(Bean.class)) {
                            BeanDefinition methodBeanDefinition = new MethodBeanDefinition(configInstance, method);
                            // 注册
                            this.registry.register(methodBean.value(), methodBeanDefinition);
                        }
                    }
                }

            }

        }
    }


}
