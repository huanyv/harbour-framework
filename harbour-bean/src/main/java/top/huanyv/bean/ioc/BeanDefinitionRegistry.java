package top.huanyv.bean.ioc;

import top.huanyv.bean.ioc.definition.BeanDefinition;
import top.huanyv.bean.ioc.definition.FactoryBeanDefinition;
import top.huanyv.bean.exception.BeanTypeNonUniqueException;
import top.huanyv.bean.utils.Assert;
import top.huanyv.bean.utils.StringUtil;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author huanyv
 * @date 2022/8/8 14:50
 */
public class BeanDefinitionRegistry implements Iterable<BeanDefinition>{

    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    /**
     * 注册一个BeanDefinition
     *
     * @param beanDefinition bean定义
     */
    public void register(BeanDefinition beanDefinition) {
        if (beanDefinition == null) {
            return;
        }
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
        if (beanDefinition == null) {
            return;
        }
        Class<?> cls = beanDefinition.getBeanClass();
        // 判断BeanName是否可用
        beanName = StringUtil.hasText(beanName) ? beanName : beanDefinition.getBeanName();
        // FactoryBean
        if (FactoryBean.class.isAssignableFrom(cls)) beanName = "&" + beanName;
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
        Assert.notNull(type, "'type' must not be null.");
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
