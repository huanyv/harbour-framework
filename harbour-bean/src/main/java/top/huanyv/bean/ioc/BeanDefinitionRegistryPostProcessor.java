package top.huanyv.bean.ioc;

/**
 * BeanDefinition后置处理器
 *
 * @author huanyv
 * @date 2022/12/22 13:40
 */
public interface BeanDefinitionRegistryPostProcessor {

    void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry);

}
