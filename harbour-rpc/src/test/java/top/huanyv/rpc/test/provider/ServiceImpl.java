package top.huanyv.rpc.test.provider;

import top.huanyv.bean.annotation.Bean;
import top.huanyv.rpc.annotation.Provider;

/**
 * @author huanyv
 * @date 2023/1/18 13:47
 */
@Provider(interfaceClass = Service.class)
//@Bean
public class ServiceImpl implements Service{
    @Override
    public String say(String name) {
        return "Hello " + name + "!";
    }
}
