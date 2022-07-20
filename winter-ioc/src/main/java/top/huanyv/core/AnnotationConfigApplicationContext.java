package top.huanyv.core;


import top.huanyv.anno.Autowired;
import top.huanyv.anno.Component;
import top.huanyv.anno.Qualifier;
import top.huanyv.anno.Value;
import top.huanyv.utils.ClassUtil;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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
       //自动装载
       autowiredBean();
//       autowiredBean(beanDefinitions);
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
       Iterator<Class<?>> iterator = classes.iterator();
       Set<BeanDefinition> beanDefinitions = new HashSet<>();
       //遍历所有的class类
       while (iterator.hasNext()){
           //遍历这些类，找到添加了Component注解的类
           Class<?> aClass = iterator.next();
           Component component = aClass.getAnnotation(Component.class);
           //如果使用了Component注解，那么可能存在注解value值不为空
           if (component != null){
               String beanName;
               if (component.value() != null && !"".equals(component.value())){
                   beanName = component.value();
               }else {
                   //获取类名首字母小写
                   String className = aClass.getName().replaceAll(aClass.getPackage().getName() + ".", "");
                   beanName = className.substring(0, 1).toLowerCase()+className.substring(1);
               }
               //封装成BeanDefinition对象
                beanDefinitions.add(new BeanDefinition(beanName, aClass));
           }
       }
       return beanDefinitions;
   }

    /**
     * 实例化对象并填充属性
     * @param beanDefinitions
     */
    private void createBean(Set<BeanDefinition> beanDefinitions){
        Iterator<BeanDefinition> iterator = beanDefinitions.iterator();
        //遍历beanDefinitions集合
        while (iterator.hasNext()){
            //获取BeanDefinition
            BeanDefinition beanDefinition = iterator.next();
            Class clazz = beanDefinition.getBeanClass();
            String beanName = beanDefinition.getBeanName();

            try {
                //通过反射实例化对象
                Object object = clazz.newInstance();
                //遍历object对象所有的属性，寻找使用Value注解的属性并填充属性值
                Field[] fields = clazz.getDeclaredFields();
                for (Field declaredField : fields) {
                    //判断是否使用Value注解，使用则给属性进行填充值
                    Value valueAnnotation = declaredField.getAnnotation(Value.class);
                    if(valueAnnotation!=null){
                        String value = valueAnnotation.value();
                        String fieldName = declaredField.getName();
                        String methodName = "set"+fieldName.substring(0, 1).toUpperCase()+fieldName.substring(1);

                        Method method = clazz.getMethod(methodName,declaredField.getType());
                        //完成数据类型转换
                        Object val = null;
                        switch (declaredField.getType().getName()){
                            case "int":
                                val = Integer.parseInt(value);
                                break;
                            case "java.lang.Integer":
                                val = Integer.parseInt(value);
                                break;
                            case "java.lang.String":
                                val = value;
                                break;
                            case "java.lang.Float":
                                val = Float.parseFloat(value);
                                break;
                        }
                        //给属性进行填充值
                        method.invoke(object, val);
                    }
                }
                beanMap.put(beanName, object);

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

    private void autowiredBean(){
        for (Map.Entry<String, Object> bean : this.beanMap.entrySet()) {
            String beanName = bean.getKey();
            Object beanInstance = bean.getValue();
            Field[] fields = beanInstance.getClass().getDeclaredFields();
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
                    field.setAccessible(true);
                    field.set(beanInstance, val);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 自动装配填充
     * @param beanDefinitions
     */
    private void autowiredBean(Set<BeanDefinition> beanDefinitions){
        Iterator<BeanDefinition> iterator = beanDefinitions.iterator();
        while (iterator.hasNext()){
            BeanDefinition beanDefinition = iterator.next();
            String beanName = beanDefinition.getBeanName();
            Class clazz = beanDefinition.getBeanClass();
            //遍历clazz类所有的字段属性，寻找使用了Autowired与Qualifier注解的属性，并填充属性值
            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field field : declaredFields) {
                //判断是否使用了Autowired与Qualifier注解
                Autowired autowired = field.getAnnotation(Autowired.class);
                if (autowired != null){
                    try {
                        Qualifier qualifier = field.getAnnotation(Qualifier.class);
                        Object val = null;
                        if (qualifier != null){
                            //beanName
                            String name = qualifier.value();
                            //从Bean容器中获取bean对象
                            val = beanMap.get(name);

                        }else {
//                            val = beanMap.get(field.getName());
                            val = getBean(field.getType());
                        }
                        //属性名称
                        String fieldName = field.getName();
                        //方法名称
                        String methodName = "set"+fieldName.substring(0,1).toUpperCase()+fieldName.substring(1);
                        Object object = beanMap.get(beanName);
                        Method method = clazz.getMethod(methodName, field.getType());
                        //进行属性填充
                        method.invoke(object,val);

                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
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
    public Object getBean(String beanName){
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

    public String[] getBeanDefinitionNames(){
        return beanNames.toArray(new String[0]);
    }

    public Integer getBeanDefinitionCount(){
        return beanNames.size();
    }
}

