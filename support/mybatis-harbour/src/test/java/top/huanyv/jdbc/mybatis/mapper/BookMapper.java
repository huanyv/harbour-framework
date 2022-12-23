package top.huanyv.jdbc.mybatis.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.huanyv.jdbc.mybatis.entity.Book;

import java.util.List;

/**
 * @author huanyv
 * @date 2022/12/22 17:10
 */
@Mapper
public interface BookMapper {

    List<Book> getAll();

}
