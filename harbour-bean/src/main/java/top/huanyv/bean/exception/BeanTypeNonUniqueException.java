package top.huanyv.bean.exception;

/**
 * @author huanyv
 * @date 2022/8/8 16:33
 */
public class BeanTypeNonUniqueException extends RuntimeException {
    private Class<?> beanClass;

    public BeanTypeNonUniqueException(Class<?> beanClass) {
        super(beanClass.getSimpleName() + " non unique");
        this.beanClass = beanClass;
    }

}
