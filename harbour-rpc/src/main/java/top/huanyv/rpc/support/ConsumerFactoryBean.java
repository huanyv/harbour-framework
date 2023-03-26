package top.huanyv.rpc.support;

import top.huanyv.bean.ioc.FactoryBean;
import top.huanyv.rpc.consumer.ConsumerProxy;
import top.huanyv.rpc.load.Balance;

/**
 * @author huanyv
 * @date 2023/1/19 16:17
 */
public class ConsumerFactoryBean implements FactoryBean {

    private Class<?> interfaceClass;

    private Balance loadBalance;

    private String serviceName;

    private int timeout;

    public ConsumerFactoryBean(Class<?> interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    public ConsumerFactoryBean(Class<?> interfaceClass, Balance loadBalance) {
        this.interfaceClass = interfaceClass;
        this.loadBalance = loadBalance;
    }

    public ConsumerFactoryBean(Class<?> interfaceClass, Balance loadBalance, String serviceName) {
        this.interfaceClass = interfaceClass;
        this.loadBalance = loadBalance;
        this.serviceName = serviceName;
    }

    public ConsumerFactoryBean(Class<?> interfaceClass, Balance loadBalance, String serviceName, Integer timeout) {
        this.interfaceClass = interfaceClass;
        this.loadBalance = loadBalance;
        this.serviceName = serviceName;
        this.timeout = timeout;
    }

    @Override
    public Object getObject() throws Exception {
        return ConsumerProxy.create(interfaceClass, loadBalance, serviceName, timeout);
    }

    @Override
    public Class<?> getObjectType() {
        return interfaceClass;
    }
}
