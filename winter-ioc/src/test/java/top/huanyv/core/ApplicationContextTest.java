package top.huanyv.core;


import com.sun.imageio.spi.OutputStreamImageOutputStreamSpi;
import org.junit.Test;
import top.huanyv.core.dao.UserDao;
import top.huanyv.core.service.BookService;
import top.huanyv.core.service.UserService;
import top.huanyv.ioc.core.AnnotationConfigApplicationContext;
import top.huanyv.ioc.core.ApplicationContext;
import top.huanyv.ioc.utils.AopUtil;


public class ApplicationContextTest {


    @Test
    public void test02() throws Exception {
        ApplicationContext app = new AnnotationConfigApplicationContext(ApplicationContextTest.class.getPackage().getName());
        UserService userService = app.getBean(UserService.class);
        userService.getUser();
        System.out.println("==================================================");
        userService.getUserById();

    }


}