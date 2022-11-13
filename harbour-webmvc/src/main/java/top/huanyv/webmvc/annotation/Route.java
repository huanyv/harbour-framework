package top.huanyv.webmvc.annotation;

import top.huanyv.webmvc.enums.RequestMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Route {

    // 请求地址
    String value() default "";

    // 请求方式
    RequestMethod[] method() default {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE};

}
