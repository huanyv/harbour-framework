package com.book.service.impl;

import com.book.aspect.DynamicDataSourceAOP;
import com.book.mapper.BookMapper;
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
    private BookMapper bookMapper;

//    @Inject
    private DataSource dataSource;

    @Override
    public Page<Book> listBook(String bname, int pageNum, int pageSize) {
        return bookMapper.listBook(bname, pageNum, pageSize);
    }

    @Override
    public int insertBook(Book book) {
//        return bookMapper.insertBook(book.getBname(), book.getAuthor()
//                , book.getPubcomp(), book.getPubdate(), book.getBcount(), book.getPrice());
        return bookMapper.insert(book);
    }

    @Override
    @Aop(TransactionAop.class)
    public int updateBook(Book book) {
//         事务测试
//        bookMapper.updateBook(book.getBname(), book.getAuthor(), book.getPubcomp(), book.getPubdate(), book.getBcount(), book.getPrice(), book.getId());
//        int i = 10 / 0;
//        return bookMapper.updateBook(book.getBname(), book.getAuthor(), book.getPubcomp(), book.getPubdate(), book.getBcount(), book.getPrice(), book.getId());
        bookMapper.updateById(book);
        int i = 10 / 0;
        return bookMapper.updateById(book);
    }

    @Override
    @Aop(TransactionAop.class)
    public int deleteBookById(Integer id) {
//        bookMapper.deleteBookById(id);
//        int i = 10 / 0;
//        return bookMapper.deleteBookById(id);
        return bookMapper.deleteById(id);
    }
}
