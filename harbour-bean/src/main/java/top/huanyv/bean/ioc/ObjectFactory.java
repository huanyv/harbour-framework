package top.huanyv.bean.ioc;

import top.huanyv.bean.exception.BeansException;

/**
 * @author huanyv
 * @date 2022/12/19 14:41
 */
public interface ObjectFactory<T> {

    T getObject() throws BeansException;

}
