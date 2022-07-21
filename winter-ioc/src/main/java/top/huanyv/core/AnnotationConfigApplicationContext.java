package top.huanyv.core;


import top.huanyv.anno.Autowired;
import top.huanyv.anno.Component;
import top.huanyv.anno.Qualifier;
import top.huanyv.anno.Value;
import top.huanyv.utils.ClassUtil;
import top.huanyv.utils.StringUtil;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class AnnotationConfigApplicationContext implements BeanFactory{

    /**
     * Bean容器
     */
    private Map<String, Object> beanMap = new ConcurrentHashMap<>();

    /**
     * BeanName集合
     */
    private List<String> beanNames = new ArrayList<>();

   public AnnotationConfigApplicationContext(String basePack){
       //遍历包，找到目标类(原材料)
       Set<BeanDefinition> beanDefinitions = findBeanDefinitions(basePack);
       //根据原材料创建bean
       createBean(beanDefinitions);

       for (Map.Entry<String, Object> beanEntry : this.beanMap.entrySet()) {
           Object bean = beanEntry.getValue();
           if (bean instanceof processRegisterBean) {
               processRegisterBean registerBean = (processRegisterBean) bean;
               registerBean.registerBean(this);
           }
       }

       //自动装载
       autowiredBean(beanDefinitions);
   }

    /**
     * 将class类封装成BeanDefinition
     * 格式：name->beanName  class->class类
     * @param basePack
     * @return BeanDefinition集合
     */
   private Set<BeanDefinition> findBeanDefinitions(String basePack){
       //获取basePack包下所有的class类
       Set<Class<?>> classes = ClassUtil.getClasses(basePack);
       Set<BeanDefinition> beanDefinitions = new HashSet<>();
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
       return beanDefinitions;
   }

    /**
     * 实例化对象并填充属性
     * @param beanDefinitions
     */
    private void createBean(Set<BeanDefinition> beanDefinitions){
        for (BeanDefinition beanDefinition : beanDefinitions) {
            String beanName = beanDefinition.getBeanName();
            Class beanClass = beanDefinition.getBeanClass();
            try {
                Constructor constructor = beanClass.getConstructor();
                constructor.setAccessible(true);
                Object beanInstance = constructor.newInstance();
                this.beanMap.put(beanName, beanInstance);
            } catch (NoSuchMethodException | IllegalAccessException
                    | InstantiationException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 自动装配填充
     * @param beanDefinitions bean定义
     */
    private void autowiredBean(Set<BeanDefinition> beanDefinitions){
        for (BeanDefinition beanDefinition : beanDefinitions) {
            String beanName = beanDefinition.getBeanName();
            Class beanClass = beanDefinition.getBeanClass();
            Field[] fields = beanClass.getDeclaredFields();
            for (Field field : fields) {
                Object val = null;
                // 注入
                if (field.isAnnotationPresent(Autowired.class)) {
                    // 名字注入
                    if (field.isAnnotationPresent(Qualifier.class)) {
                        val = getBean(field.getName());
                    } else { // 根据类型注入
                        val = getBean(field.getType());
                    }
                }
                try {
                    Object bean = getBean(beanName);
                    field.setAccessible(true);
                    field.set(bean, val);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 根据名称获取bean
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
                return (T) beanMap.get(entry.getKey());
            }
        }
        return null;
    }

    @Override
    public boolean containsBean(String name) {
        return this.beanMap.containsKey(name);
    }

    public <T> void register(String beanName, T t) {
        this.beanMap.put(beanName, t);
    }

    public String[] getBeanDefinitionNames(){
        return new ArrayList<>(this.beanMap.keySet()).toArray(new String[this.beanMap.size()]);
    }

    public Integer getBeanDefinitionCount(){
        return this.beanMap.size();
    }
}

