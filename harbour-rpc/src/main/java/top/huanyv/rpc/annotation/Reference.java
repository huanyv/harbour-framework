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

    /**
     * 负载均衡策略
     *
     * @return {@link Balance}
     */
    Balance loadBalance() default Balance.RANDOM;

    /**
     * 超时时间，单位秒
     *
     * @return int
     */
    int timeout() default 3;
}
