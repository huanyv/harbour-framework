package com.book.mapper;

import com.book.pojo.Book;
import top.huanyv.jdbc.annotation.Dao;
import top.huanyv.jdbc.builder.*;
import top.huanyv.jdbc.util.Page;
import top.huanyv.tools.utils.StringUtil;

/**
 * @author admin
 * @date 2022/8/3 9:15
 */
@Dao
public class BookDaoImpl implements BookDao {

    @Override
    public Page<Book> listBook(String bname, int pageNum, int pageSize) {
        Page<Book> page = new Select().from(Book.class)
                .where(condition -> condition.and(StringUtil.hasText(bname), "bname like ?", "%" + bname + "%"))
                .page(pageNum, pageSize);
        return page;
    }

    @Override
    @top.huanyv.jdbc.annotation.Insert("insert into t_book(bname, author, pubcomp, pubdate, bcount, price) values(#{bname}, #{author}, #{pubcomp}, #{pubdate}, #{bcount}, #{price})")
    public int insertBook(Book book) {
        return new Insert(Book.class)
                .columns("bname, author, pubcomp, pubdate, bcount, price")
                .values(book.getBname(), book.getAuthor(), book.getPubcomp(),
                        book.getPubdate(), book.getBcount(), book.getPrice()).update();
    }

    @Override
    public int updateBook(Book book) {
        int update = new Update(Book.class)
                .append("bname = ?", book.getBname())
                .append("author = ?", book.getAuthor())
                .append("pubcomp = ?", book.getPubcomp())
                .append("pubdate = ?", book.getPubdate())
                .append("bcount = ?", book.getBcount())
                .append("price = ?", book.getPrice())
                .where(condition -> condition.append("id = ?", book.getId())).update();
        return update;
    }

    @Override
//    @top.huanyv.jdbc.annotation.Delete("delete from t_book where id = ?")
    public int deleteBookById(Integer id) {
        return new Delete().from(Book.class)
                .where(condition -> condition.append("id = ?", id))
                .update();
    }
}
