package top.huanyv.rpc.test.config;

import top.huanyv.bean.annotation.Bean;
import top.huanyv.bean.annotation.Configuration;
import top.huanyv.rpc.support.RpcInitializer;

/**
 * @author huanyv
 * @date 2023/1/19 16:01
 */
@Configuration
public class Config {

    @Bean
    public RpcInitializer rpcConfiguration() {
        RpcInitializer rpcConfiguration = new RpcInitializer();
        rpcConfiguration.setServiceAddress("127.0.0.1:20880");
        rpcConfiguration.setScanPackages("top.huanyv.rpc.test");
        return rpcConfiguration;
    }
}
