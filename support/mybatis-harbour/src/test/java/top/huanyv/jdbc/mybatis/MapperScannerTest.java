package top.huanyv.jdbc.mybatis;

import org.junit.Test;
import top.huanyv.bean.ioc.AnnotationConfigApplicationContext;
import top.huanyv.bean.ioc.ApplicationContext;
import top.huanyv.jdbc.mybatis.entity.Book;
import top.huanyv.jdbc.mybatis.service.BookService;

import java.util.List;

public class MapperScannerTest {

    @Test
    public void test() {
        ApplicationContext app = new AnnotationConfigApplicationContext(MapperScannerTest.class.getPackage().getName());
        BookService bookService = app.getBean(BookService.class);
        List<Book> bookList = bookService.getAll();
        System.out.println("bookList = " + bookList);
    }

}