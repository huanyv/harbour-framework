package top.huanyv.start.loader.rpc;

import top.huanyv.bean.annotation.Bean;
import top.huanyv.bean.ioc.ApplicationContext;
import top.huanyv.bean.utils.BeanFactoryUtil;
import top.huanyv.rpc.register.ZookeeperRegistry;
import top.huanyv.rpc.support.RpcInitializer;
import top.huanyv.rpc.util.RpcConfigurer;
import top.huanyv.start.anntation.Conditional;
import top.huanyv.start.anntation.ConfigurationProperties;
import top.huanyv.start.config.AppArguments;
import top.huanyv.start.loader.ApplicationLoader;
import top.huanyv.start.loader.Condition;

/**
 * @author huanyv
 * @date 2023/1/22 16:01
 */
@ConfigurationProperties(prefix = "harbour.rpc")
public class RpcStartLoader implements ApplicationLoader {

    /**
     * 当前服务地址
     */
    private String service;

    /**
     * 注册中心地址
     */
    private String registry;

    /**
     * 服务扫描包
     */
    private String scanPackages;

    @Override
    public void load(ApplicationContext applicationContext, AppArguments appArguments) {
        RpcConfigurer configurer = RpcConfigurer.create();
        configurer.setRegistry(new ZookeeperRegistry(this.registry));
    }

    @Bean
    @Conditional(ConditionOnMissingBean.class)
    public RpcInitializer rpcInitializer() {
        RpcInitializer rpcInitializer = new RpcInitializer();
        rpcInitializer.setServiceAddress(service);
        rpcInitializer.setScanPackages(this.scanPackages);
        return rpcInitializer;
    }

    public static class ConditionOnMissingBean implements Condition {

        @Override
        public boolean matchers(ApplicationContext applicationContext, AppArguments appArguments) {
            return BeanFactoryUtil.isNotPresent(applicationContext, RpcInitializer.class);
        }
    }
}
