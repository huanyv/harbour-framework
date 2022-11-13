package com.book.pojo.vo;

import com.book.pojo.Book;
import lombok.Data;

@Data
public class BookVo extends Book {

    private Integer limit;
    private Integer page;

}
