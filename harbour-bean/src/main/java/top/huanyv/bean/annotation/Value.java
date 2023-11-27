package top.huanyv.bean.annotation;


import java.lang.annotation.*;

/**
 * 给属性注入配置值，只支持基本数据类型和字符串
 *
 * @author huanyv
 * @date 2023/11/16
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Value {
    /**
     * 配置名称
     *
     * @return {@link String}
     */
    String value();
}

