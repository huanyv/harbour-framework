package com.book.mapper;

import com.book.pojo.Book;
import top.huanyv.jdbc.builder.BaseDao;
import top.huanyv.jdbc.util.Page;


public interface BookDao extends BaseDao<Book> {

    /**
     * 查询图书
     * @return
     */
    Page<Book> listBook(String bname, int pageNUm, int pageSize);

    /**
     * 添加图书
     * @return
     */
    int insertBook(Book book);

    /**
     * 修改图书
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
