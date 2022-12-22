package top.huanyv.bean.exception;

/**
 * @author huanyv
 * @date 2022/11/3 9:47
 */
public class NoSuchBeanDefinitionException extends BeansException {

    public NoSuchBeanDefinitionException(String message) {
        super(message);
    }
}
