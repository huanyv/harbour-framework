package top.huanyv.bean.exception;

/**
 * @author huanyv
 * @date 2022/11/3 10:16
 */
public class BeanCurrentlyInCreationException extends BeansException {

    public BeanCurrentlyInCreationException(String beanName) {
        super("Error creating bean with name '" + beanName + "': " +
                "Bean is currently in creation: Is there an unresolvable circular reference?");
    }
}
