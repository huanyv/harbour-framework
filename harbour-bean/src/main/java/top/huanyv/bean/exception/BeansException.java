package top.huanyv.bean.exception;

/**
 * @author huanyv
 * @date 2022/12/22 16:03
 */
public class BeansException extends RuntimeException {

    public BeansException() {
    }

    public BeansException(String message) {
        super(message);
    }

    public BeansException(String message, Throwable cause) {
        super(message, cause);
    }

}
