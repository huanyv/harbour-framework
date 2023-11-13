package top.huanyv.bean.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 属性注入，value值为空代表使用类型注入，反之使用BeanName注入
 *
 * @author huanyv
 * @date 2023/11/12
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Inject {
    String value() default "";
}
