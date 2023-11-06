package top.huanyv.bean.test;

import org.junit.Test;
import top.huanyv.bean.aop.AopContext;
import top.huanyv.bean.ioc.AnnotationConfigApplicationContext;
import top.huanyv.bean.ioc.ApplicationContext;
import top.huanyv.bean.test.aop.AdminService;
import top.huanyv.bean.test.cycle.A;
import top.huanyv.bean.test.cycle.B;
import top.huanyv.bean.test.entity.User;
import top.huanyv.bean.test.ioc.controller.UserController;
import top.huanyv.bean.test.factory.BookDao;
import top.huanyv.bean.test.ioc.dao.UserDao;
import top.huanyv.bean.utils.AopUtil;
import top.huanyv.bean.utils.BeanFactoryUtil;

import java.util.Arrays;

/**
 * @author huanyv
 * @date 2022/11/18 14:18
 */
public class ApplicationContextTest {

    @Test
    public void testIOC() {
        ApplicationContext app = new AnnotationConfigApplicationContext("top.huanyv.bean.test.ioc");
        UserController userController = (UserController) app.getBean("userController");
        User user = userController.getUserById(2);
        System.out.println("user = " + user);
        System.out.println("app.getBean(UserController.class) = " + app.getBean(UserController.class));
    }

    @Test
    public void testAOP() {
        ApplicationContext app = new AnnotationConfigApplicationContext("top.huanyv.bean.test.aop");
        AdminService adminService = app.getBean(AdminService.class);
        User user = adminService.getUser();
        System.out.println(user);
        System.out.println(AopUtil.getTargetClass(adminService));
    }

    @Test
    public void testFactoryBean() throws NoSuchMethodException {
        ApplicationContext app = new AnnotationConfigApplicationContext("top.huanyv.bean.test.factory");
        UserDao userDao = app.getBean(UserDao.class);
        System.out.println("userDao = " + userDao);
        System.out.println("userDao.getClass() = " + userDao.getClass());
        System.out.println(AopUtil.getTargetClass(userDao));
        System.out.println("app.getBean(BookDao.class) = " + app.getBean(BookDao.class));
        System.out.println("app.getBean(\"&mapperFactoryBean\") = " + app.getBean("&mapperFactoryBean"));

        AopContext aopContext = new AopContext();
        System.out.println("aopContext.hasProxy(UserDao.class) = " + aopContext.hasProxy(UserDao.class));
        System.out.println(aopContext.hasProxy(UserDao.class, UserDao.class.getMethod("getUserById", Integer.class)));
    }

    @Test
    public void testCycle() {
        ApplicationContext app = new AnnotationConfigApplicationContext("top.huanyv.bean.test.cycle");
        A aBean = app.getBean(A.class);
        System.out.println(aBean);
        System.out.println("aBean.getClass() = " + aBean.getClass());
        B bBean = app.getBean(B.class);
        System.out.println(bBean);
        System.out.println("bBean.getClass() = " + bBean.getClass());
    }

    @Test
    public void testIOCMethod() {
        ApplicationContext app = new AnnotationConfigApplicationContext("top.huanyv.bean.test.ioc");
        app.register(Bean.class);
        app.refresh();
        System.out.println("app.getBeanDefinitionNames() = " + Arrays.toString(app.getBeanDefinitionNames()));
        System.out.println("app.getBeanDefinitionCount() = " + app.getBeanDefinitionCount());
        System.out.println("app.getBeanDefinition(\"bean\") = " + app.getBeanDefinition("bean"));
        System.out.println("app.containsBean(\"bean\") = " + app.containsBean("bean"));

        System.out.println(BeanFactoryUtil.getBeansByType(app, Bean.class));
        System.out.println("BeanFactoryUtil.isNotPresent(app, Bean.class) = " + BeanFactoryUtil.isNotPresent(app, Bean.class));
        System.out.println("BeanFactoryUtil.isPresent(app, Bean.class) = " + BeanFactoryUtil.isPresent(app, Bean.class));
        System.out.println("BeanFactoryUtil.getBeanClasses(app) = " + BeanFactoryUtil.getBeanClasses(app));
        System.out.println("BeanFactoryUtil.getBeans(app) = " + BeanFactoryUtil.getBeans(app));
        System.out.println("BeanFactoryUtil.getBeanMap(app) = " + BeanFactoryUtil.getBeanMap(app));
    }

}


class Bean {
    private String name;

    public Bean() {
    }

    public Bean(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Bean{" +
                "name='" + name + '\'' +
                '}';
    }
}
