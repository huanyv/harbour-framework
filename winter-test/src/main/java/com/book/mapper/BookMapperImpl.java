package com.book.mapper;

import com.book.pojo.Book;
import com.mysql.jdbc.Driver;
import top.huanyv.ioc.anno.Autowired;
import top.huanyv.jdbc.anno.Mapper;
import top.huanyv.jdbc.builder.Delete;
import top.huanyv.jdbc.builder.Insert;
import top.huanyv.jdbc.builder.Select;
import top.huanyv.jdbc.builder.Update;
import top.huanyv.jdbc.core.ConnectionHolder;
import top.huanyv.jdbc.core.Page;
import top.huanyv.jdbc.extend.SimpleDataSource;
import top.huanyv.utils.StringUtil;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * @author admin
 * @date 2022/8/3 9:15
 */
@Mapper
public class BookMapperImpl implements BookMapper {

    @Override
    public Page<Book> listBook(String bname, int pageNum, int pageSize) {
        Page<Book> page = new Select().from(Book.class)
                .where().append("1 = 1")
                .and(StringUtil.hasText(bname), "bname like ?", "%" + bname + "%")
                .page(pageNum, pageSize);
        return page;
    }

    @Override
    public int insertBook(String bname, String author, String pubcomp, Date pubdate, int bcount, BigDecimal price) {
        return new Insert(Book.class).columns("bname", "author", "pubcomp", "pubdate", "bcount", "price")
                .values(bname, author, pubcomp, pubdate, bcount, price).update();
    }

    @Override
    public int updateBook(String bname, String author, String pubcomp, Date pubdate, int bcount, BigDecimal price, Integer id) {
        return new Update(Book.class)
                .append("bname = ?", bname)
                .append("author = ?", author)
                .append("pubcomp = ?", pubcomp)
                .append("pubdate = ?", pubdate)
                .append("bcount = ?", bcount)
                .append("price = ?", price)
                .where().append("id = ?", id).update();
    }

    @Override
    public int deleteBookById(Integer id) {
        return new Delete().from(Book.class).where().append("id = ?", id).update();
    }
}
