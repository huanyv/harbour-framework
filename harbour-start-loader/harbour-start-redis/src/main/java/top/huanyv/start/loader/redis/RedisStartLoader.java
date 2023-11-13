package top.huanyv.start.loader.redis;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import top.huanyv.bean.annotation.Bean;
import top.huanyv.bean.ioc.ApplicationContext;
import top.huanyv.bean.ioc.Configuration;
import top.huanyv.bean.utils.BeanFactoryUtil;
import top.huanyv.start.anntation.Conditional;
import top.huanyv.start.anntation.Properties;
import top.huanyv.start.config.AppArguments;
import top.huanyv.start.loader.ApplicationLoader;
import top.huanyv.start.loader.Condition;

import java.time.Duration;

/**
 * @author huanyv
 * @date 2023/7/22 17:36
 */
@Properties(prefix = "redis.")
public class RedisStartLoader implements ApplicationLoader {

    private String host = "127.0.0.1";

    private int port = 6379;

    private int maxIdle = 8;

    private int minIdle = 0;

    private long maxWait = 500;

    private JedisPool jedisPool;

    @Override
    public void load(ApplicationContext applicationContext, Configuration configuration) {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(this.maxIdle);
        jedisPoolConfig.setMinIdle(this.minIdle);
        jedisPoolConfig.setMaxWait(Duration.ofMillis(this.maxWait));
        this.jedisPool = new JedisPool(jedisPoolConfig, host, port);
    }

    @Bean
    @Conditional(ConditionOnMissingJedisPoolBean.class)
    public JedisPool jedisPool() {
        return this.jedisPool;
    }

    @Bean
    @Conditional(ConditionOnMissingJedisBean.class)
    public Jedis jedis() {
        // 创建代理对象
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(Jedis.class);
        enhancer.setCallback((MethodInterceptor) (o, method, args, methodProxy) -> {
            Jedis jedis = this.jedisPool.getResource();
            Object result = method.invoke(jedis, args);
            jedis.close();
            return result;
        });
        return (Jedis) enhancer.create();
    }

    public static class ConditionOnMissingJedisBean implements Condition {
        @Override
        public boolean matchers(ApplicationContext applicationContext, Configuration configuration) {
            return BeanFactoryUtil.isNotPresent(applicationContext, Jedis.class);
        }
    }


    public static class ConditionOnMissingJedisPoolBean implements Condition {

        @Override
        public boolean matchers(ApplicationContext applicationContext, Configuration configuration) {
            return BeanFactoryUtil.isNotPresent(applicationContext, JedisPool.class);
        }
    }
}
