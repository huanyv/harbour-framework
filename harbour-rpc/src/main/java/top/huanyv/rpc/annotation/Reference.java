package top.huanyv.rpc.annotation;

import top.huanyv.rpc.load.Balance;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Reference {

    /**
     * 服务名
     *
     * @return {@link String}
     */
    String value() default "";

    Balance loadBalance() default Balance.RANDOM;
}
