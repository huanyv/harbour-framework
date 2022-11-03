package top.huanyv.ioc.exception;

/**
 * @author huanyv
 * @date 2022/11/3 9:47
 */
public class NoSuchBeanDefinitionException extends RuntimeException {

    private Class<?> beanClass;

    public NoSuchBeanDefinitionException(Class<?> type) {
        super("No qualifying bean of type ' " + type.getName() + " ' available");
        this.beanClass = type;
    }

    public NoSuchBeanDefinitionException(String message) {
        super(message);
    }
}
