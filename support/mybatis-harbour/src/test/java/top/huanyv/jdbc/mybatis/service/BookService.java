package top.huanyv.jdbc.mybatis.service;

import top.huanyv.bean.annotation.Bean;
import top.huanyv.bean.annotation.Inject;
import top.huanyv.jdbc.mybatis.entity.Book;
import top.huanyv.jdbc.mybatis.mapper.BookMapper;

import java.util.List;

/**
 * @author huanyv
 * @date 2022/12/22 17:13
 */
@Bean
public class BookService {

    @Inject
    private BookMapper bookMapper;

    public List<Book> getAll() {
        return bookMapper.getAll();
    }

}
