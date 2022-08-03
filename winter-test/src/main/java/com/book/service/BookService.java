package com.book.service;

import com.book.pojo.Book;
import com.book.pojo.vo.BookVo;
import top.huanyv.jdbc.core.Page;

import java.util.List;

public interface BookService {

    /**
     * 查询图书
     * @return
     */
    Page<Book> listBook(String bname, int pageNUm, int pageSize);

    /**
     * 添加图书
     * @param book
     * @return
     */
    int insertBook(Book book);

    /**
     * 修改图书
     * @param book
     * @return
     */
    int updateBook(Book book);

    /**
     * 根据ID删除图书
     * @param id
     * @return
     */
    int deleteBookById(Integer id);

}
