package top.huanyv.rpc.support;

import top.huanyv.bean.annotation.Bean;
import top.huanyv.bean.annotation.Inject;
import top.huanyv.bean.ioc.ApplicationContext;
import top.huanyv.bean.ioc.ApplicationContextAware;
import top.huanyv.bean.ioc.BeanDefinitionRegistry;
import top.huanyv.bean.ioc.BeanDefinitionRegistryPostProcessor;
import top.huanyv.bean.ioc.definition.BeanDefinition;
import top.huanyv.bean.ioc.definition.ClassBeanDefinition;
import top.huanyv.rpc.annotation.Reference;
import top.huanyv.rpc.provider.ProviderServer;
import top.huanyv.bean.utils.ClassUtil;
import top.huanyv.bean.utils.StringUtil;

import java.lang.reflect.Field;
import java.util.Set;

/**
 * @author huanyv
 * @date 2023/1/19 15:36
 */
public class RpcInitializer implements BeanDefinitionRegistryPostProcessor, ApplicationContextAware {

    private static ApplicationContext context;

    // 服务地址
    private String serviceAddress;

    // 服务扫描包
    private String scanPackages;

    public RpcInitializer() {
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) {
        // Reference
        Set<Class<?>> classes = ClassUtil.getClassesByAnnotation(Bean.class, scanPackages);
        for (Class<?> cls : classes) {
            for (Field field : cls.getDeclaredFields()) {
                Reference reference = field.getAnnotation(Reference.class);
                if (reference != null && field.isAnnotationPresent(Inject.class)) {
                    Class<?> type = field.getType();
                    String beanName = StringUtil.firstLetterLower(type.getSimpleName());
                    BeanDefinition beanDefinition = new ClassBeanDefinition(ConsumerFactoryBean.class,
                            type, reference.loadBalance(), reference.value(), reference.timeout());
                    registry.register(beanName, beanDefinition);
                }
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        context = applicationContext;
        if (StringUtil.hasText(serviceAddress)) {
            ProviderServer server = new ContextProviderServer(scanPackages, context);
            server.start(serviceAddress);
        }
    }

    public void setServiceAddress(String serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

    public void setScanPackages(String scanPackages) {
        this.scanPackages = scanPackages;
    }

}
