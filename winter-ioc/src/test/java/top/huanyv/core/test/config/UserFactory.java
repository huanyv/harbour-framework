package top.huanyv.core.test.config;

import top.huanyv.core.test.entity.User;
import top.huanyv.ioc.anno.Component;
import top.huanyv.ioc.anno.Scope;
import top.huanyv.ioc.core.FactoryBean;
import top.huanyv.ioc.core.definition.BeanDefinition;

/**
 * @author huanyv
 * @date 2022/11/3 10:56
 */
//@Component("user")
//@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class UserFactory implements FactoryBean<User> {

    private String userName;

    public UserFactory() {
    }
    public UserFactory(String userName) {
        this.userName = userName;
    }

    @Override
    public User getObject() throws Exception {
        User user = new User();
        user.setUsername(this.userName);
        return user;
    }

    @Override
    public Class<?> getObjectType() {
        return User.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    @Override
    public String toString() {
        return "UserFactory{" +
                "userName='" + userName + '\'' +
                '}';
    }
}
