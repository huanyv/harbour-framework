package top.huanyv.bean.test.weave;

import top.huanyv.bean.annotation.Component;
import top.huanyv.bean.ioc.ApplicationContext;
import top.huanyv.bean.ioc.ApplicationContextWeave;
import top.huanyv.bean.test.factory.MapperFactoryBean;
import top.huanyv.bean.test.ioc.dao.UserDao;

/**
 * @author huanyv
 * @date 2022/11/18 15:30
 */
@Component
public class BeanWeave implements ApplicationContextWeave {
    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public void createBeanInstanceAfter(ApplicationContext applicationContext) {
        System.out.println("创建实例后");
        applicationContext.registerBean(MapperFactoryBean.class, UserDao.class);
        applicationContext.refresh();
    }

    @Override
    public void populateBeanBefore(ApplicationContext applicationContext) {
        System.out.println("注入之前");
    }

    @Override
    public void populateBeanAfter(ApplicationContext applicationContext) {
        System.out.println("注入之后");
    }
}
