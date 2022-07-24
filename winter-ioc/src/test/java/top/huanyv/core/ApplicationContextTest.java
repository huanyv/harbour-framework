package top.huanyv.core;


import org.junit.Test;
import top.huanyv.core.dao.UserDao;
import top.huanyv.core.service.BookService;
import top.huanyv.core.service.UserService;
import top.huanyv.ioc.core.AnnotationConfigApplicationContext;


public class ApplicationContextTest {

    @Test
    public void getBean() {
        AnnotationConfigApplicationContext app
                = new AnnotationConfigApplicationContext(ApplicationContextTest.class.getPackage().getName());

//        AnnotationConfigApplicationContext app
//                = new AnnotationConfigApplicationContext(ClassLoader.getSystemResourceAsStream("application.properties"));

        UserService userService = (UserService) app.getBean("userService");
        userService.getUser();

        System.out.println("app.getBean(\"string\") = " + app.getBean("string"));
        System.out.println("app.getBean(\"string\") = " + app.getBean("string2"));


        BookService bookService = app.getBean(BookService.class);
        System.out.println("bookService = " + bookService.getUserService());
        System.out.println("userService = " + userService.getBookService());

    }

}