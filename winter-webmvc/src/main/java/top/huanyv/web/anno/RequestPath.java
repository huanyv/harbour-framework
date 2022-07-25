package top.huanyv.web.anno;

import top.huanyv.web.enums.RequestMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestPath {

    // 请求地址
    String value() default "";

    // 请求方式
    RequestMethod method() default RequestMethod.GET;

}
