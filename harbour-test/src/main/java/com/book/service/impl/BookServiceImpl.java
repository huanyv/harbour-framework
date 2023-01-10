package com.book.service.impl;

import com.book.mapper.BookDao;
import com.book.pojo.Book;
import com.book.service.BookService;
import top.huanyv.bean.annotation.Component;
import top.huanyv.bean.annotation.Inject;
import top.huanyv.bean.aop.Aop;
import top.huanyv.jdbc.support.TransactionAop;
import top.huanyv.jdbc.util.Page;

import javax.sql.DataSource;

@Component
public class BookServiceImpl implements BookService {

    @Inject
    private BookDao bookDao;

//    @Inject
    private DataSource dataSource;

    @Override
    public Page<Book> listBook(String bname, int pageNum, int pageSize) {
        return bookDao.listBook(bname, pageNum, pageSize);
    }

    @Override
    public int insertBook(Book book) {
        return bookDao.insert(book);
    }

    @Override
    @Aop(TransactionAop.class)
    public int updateBook(Book book) {
//         事务测试
//        bookDao.updateBook(book);
//        int i = 10 / 0;
//        return bookDao.updateBook(book);
        bookDao.updateById(book);
        // 模拟异常
        int i = 10 / 0;
        return bookDao.updateById(book);
    }

    @Override
    public int deleteBookById(Integer id) {
        return bookDao.deleteById(id);
    }
}
