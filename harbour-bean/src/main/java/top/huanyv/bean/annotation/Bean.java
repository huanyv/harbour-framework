package top.huanyv.bean.annotation;

import java.lang.annotation.*;

/**
 * 声明一个Bean，放入IOC容器中
 *
 * @author huanyv
 * @date 2023/11/06
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Bean {
    /**
     * IOC容器中的BeanName
     *
     * @return {@link String}
     */
    String value() default "";

    /**
     * 是否多实例
     *
     * @return boolean
     */
    boolean prototype() default false;

    /**
     * 是否懒加载
     *
     * @return boolean
     */
    boolean lazy() default false;
}
