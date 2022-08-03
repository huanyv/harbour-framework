package com.book.service.impl;

import com.book.mapper.BookMapper;
import com.book.pojo.Book;
import com.book.pojo.vo.BookVo;
import com.book.service.BookService;
import top.huanyv.ioc.anno.Autowired;
import top.huanyv.ioc.anno.Component;
import top.huanyv.jdbc.core.Page;

import java.util.List;

@Component
public class BookServiceImpl implements BookService {

    @Autowired
    private BookMapper bookMapper;

    @Override
    public Page<Book> listBook(String bname, int pageNum, int pageSize) {
        return bookMapper.listBook(bname, pageNum, pageSize);
    }

    @Override
    public int insertBook(Book book) {
        return bookMapper.insertBook(book.getBname(), book.getAuthor()
                , book.getPubcomp(), book.getPubdate(), book.getBcount(), book.getPrice());
    }

    @Override
    public int updateBook(Book book) {
        return bookMapper.updateBook(book.getBname(), book.getAuthor(), book.getPubcomp(), book.getPubdate(), book.getBcount(), book.getPrice(), book.getId());
    }

    @Override
    public int deleteBookById(Integer id) {
        return bookMapper.deleteBookById(id);
    }
}
