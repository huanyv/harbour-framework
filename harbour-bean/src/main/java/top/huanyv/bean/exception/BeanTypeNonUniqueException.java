package top.huanyv.bean.exception;

/**
 * @author huanyv
 * @date 2022/8/8 16:33
 */
public class BeanTypeNonUniqueException extends BeansException {

    public BeanTypeNonUniqueException(Class<?> beanClass) {
        super(beanClass.getSimpleName() + " non unique");
    }

}
