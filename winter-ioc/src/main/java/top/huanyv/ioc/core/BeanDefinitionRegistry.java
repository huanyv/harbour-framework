package top.huanyv.ioc.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author huanyv
 * @date 2022/8/8 14:50
 */
public class BeanDefinitionRegistry {

    private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

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

}
