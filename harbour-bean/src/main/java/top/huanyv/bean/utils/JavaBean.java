package top.huanyv.bean.utils;

import top.huanyv.bean.utils.BeanUtil;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 表明一个类是不是一个JavaBean <br />
 * 当使用了此注解后，{@link BeanUtil#isJavaBean(Class)}方法，直接使用此注解判断
 *
 * @author huanyv
 * @date 2022/11/17
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface JavaBean {

    /**
     * 是否是一个JavaBean
     * @return true:是一个JavaBean false:不是JavaBean
     */
    boolean value() default true;

}
