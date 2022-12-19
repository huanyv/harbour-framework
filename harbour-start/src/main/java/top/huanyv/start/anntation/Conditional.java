package top.huanyv.start.anntation;

import top.huanyv.start.loader.Condition;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Conditional {
    Class<? extends Condition> value();
}
