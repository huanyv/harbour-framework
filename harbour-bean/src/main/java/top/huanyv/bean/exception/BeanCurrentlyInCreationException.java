package top.huanyv.bean.exception;

/**
 * @author huanyv
 * @date 2022/11/3 10:16
 */
public class BeanCurrentlyInCreationException extends RuntimeException{

    public BeanCurrentlyInCreationException(String beanName) {
        super(" Error creating bean with name '" + beanName + "': " +
                "Requested bean is currently in creation: Is there an unresolvable circular reference?");
    }
}
