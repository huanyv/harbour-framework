package top.huanyv.web.anno;

import jdk.nashorn.internal.ir.annotations.Reference;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author admin
 * @date 2022/7/27 17:18
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Guard {
    String[] value();
    String[] exclude();
}
