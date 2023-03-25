package top.huanyv.webmvc.security;


import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface HasPermission {

    String[] value() default "";

}
