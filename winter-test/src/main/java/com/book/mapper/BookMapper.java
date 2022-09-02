package com.book.mapper;

import com.book.pojo.Book;
import com.book.pojo.vo.BookVo;
import top.huanyv.jdbc.anno.*;
import top.huanyv.jdbc.builder.BaseDao;
import top.huanyv.jdbc.core.Page;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

//@Mapper
public interface BookMapper extends BaseDao<Book> {

    /**
     * 查询图书
     * @return
     */
    @Select("select * from t_book")
    Page<Book> listBook(String bname, int pageNUm, int pageSize);

    /**
     * 添加图书
     * @return
     */
    @Insert("INSERT INTO t_book (bname, author, pubcomp, pubdate, bcount, price) values(?, ?, ?, ?, ?, ?)")
    int insertBook(String bname, String author, String pubcomp, Date pubdate, int bcount, BigDecimal price);

    /**
     * 修改图书
     * @return
     */
    @Update("UPDATE t_book SET bname = ?, author=?, pubcomp=?, pubdate=?,bcount=?, price=? WHERE id = ?")
    int updateBook(String bname, String author, String pubcomp, Date pubdate, int bcount, BigDecimal price, Integer id);

    /**
     * 根据ID删除图书
     * @param id
     * @return
     */
    @Delete("delete from t_book where id = ?")
    int deleteBookById(Integer id);

}
