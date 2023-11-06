package com.book.dao.impl;

import com.book.dao.BookDao;
import com.book.pojo.Book;
import top.huanyv.jdbc.annotation.Dao;
import top.huanyv.jdbc.annotation.Delete;
import top.huanyv.jdbc.annotation.Insert;
import top.huanyv.jdbc.builder.*;
import top.huanyv.jdbc.core.SqlContext;
import top.huanyv.jdbc.core.SqlContextManager;
import top.huanyv.jdbc.core.pagination.Page;
import top.huanyv.bean.utils.StringUtil;

/**
 * @author admin
 * @date 2022/8/3 9:15
 */
@Dao
public class BookDaoImpl implements BookDao {

    private final SqlContext sqlContext = new SqlContextManager();

    @Override
    public Page<Book> listBook(String bname, int pageNum, int pageSize) {
        SqlBuilder sb = new SqlBuilder("select * from t_book")
                .condition("where", c -> c
                        .append(StringUtil.hasText(bname), "bname like ?", "%" + bname + "%")
                );
        Page<Book> page = new Page<>(pageNum, pageSize);
        sqlContext.selectPage(page, Book.class, sb.getSql(), sb.getArgs());
        return page;
    }

    @Override
    @Insert("insert into t_book(bname, author, pubcomp, pubdate, bcount, price) values(#{bname}, #{author}, #{pubcomp}, #{pubdate}, #{bcount}, #{price})")
    public int insertBook(Book book) {
        return 0;
    }

    @Override
    public int updateBook(Book book) {
        SqlBuilder sb = new SqlBuilder("update t_book set")
                .join(", ", j -> j
                        .append(StringUtil.hasText(book.getBname()), "bname = #{bname}")
                        .append("author = #{author}")
                        .append("pubcomp = #{pubcomp}")
                        .append("pubdate = #{pubdate}")
                        .append("bcount = #{bcount}")
                        .append("price = #{price}")
                )
                .condition("where", condition -> condition.append("id = #{id}"));
        return sqlContext.update(sb.getSql(), book);
    }

    @Override
    @Delete("delete from t_book where id = ?")
    public int deleteBookById(Integer id) {
        return 0;
    }
}
