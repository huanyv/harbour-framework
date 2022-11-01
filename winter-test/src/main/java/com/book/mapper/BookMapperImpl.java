package com.book.mapper;

import com.book.pojo.Book;
import top.huanyv.jdbc.anno.Dao;
import top.huanyv.jdbc.builder.*;
import top.huanyv.jdbc.core.Page;
import top.huanyv.utils.StringUtil;

import java.math.BigDecimal;
import java.util.Date;
import java.util.function.Consumer;

/**
 * @author admin
 * @date 2022/8/3 9:15
 */
@Dao
public class BookMapperImpl implements BookMapper {

    @Override
    public Page<Book> listBook(String bname, int pageNum, int pageSize) {
        Page<Book> page = new Select().from(Book.class)
                .where(condition -> condition.and(StringUtil.hasText(bname), "bname like ?", "%" + bname + "%"))
                .page(pageNum, pageSize);
        return page;
    }

    @Override
    public int insertBook(String bname, String author, String pubcomp, Date pubdate, int bcount, BigDecimal price) {
        return new Insert(Book.class)
//                .columns("bname", "author", "pubcomp", "pubdate", "bcount", "price")
                .columns("bname, author, pubcomp, pubdate, bcount, price")
                .values(bname, author, pubcomp, pubdate, bcount, price).update();
    }

    @Override
    public int updateBook(String bname, String author, String pubcomp, Date pubdate, int bcount, BigDecimal price, Integer id) {
        int update = new Update(Book.class)
                .append("bname = ?", bname)
                .append("author = ?", author)
                .append("pubcomp = ?", pubcomp)
                .append("pubdate = ?", pubdate)
                .append("bcount = ?", bcount)
                .append("price = ?", price)
                .where(condition -> condition.append("id = ?", id)).update();
        return update;
    }

    @Override
    public int deleteBookById(Integer id) {
        return new Delete().from(Book.class)
                .where(condition -> condition.append("id = ?", id))
                .update();
    }
}
