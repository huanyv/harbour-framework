package com.book.controller;

import com.book.aspect.LogAop;
import com.book.pojo.Book;
import com.book.pojo.ResponseResult;
import com.book.service.BookService;

import top.huanyv.bean.annotation.Component;
import top.huanyv.bean.annotation.Inject;
import top.huanyv.bean.aop.Aop;
import top.huanyv.jdbc.core.pagination.Page;
import top.huanyv.webmvc.annotation.*;
import top.huanyv.webmvc.annotation.argument.Body;
import top.huanyv.webmvc.annotation.argument.Param;
import top.huanyv.webmvc.annotation.argument.Path;

@Component
@Route("/admin/book")
@Aop(LogAop.class)
//@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Body
public class BookController {

    @Inject
    private BookService bookService;

    @Get
    public ResponseResult listBook(@Param("bname") String bname, @Param("page") int pageNum, @Param("limit") int pageSize) {
        // 异常处理器测试
//        Object o = null;
//        System.out.println(o.toString());
        Page<Book> bookPage = bookService.listBook(bname, pageNum, pageSize);
        return new ResponseResult(0, "", bookPage.getTotal(), bookPage.getData());
    }

    /**
     * 添加图书
     */
    @Post
    public ResponseResult addBook(@Body Book book) {
        int i = bookService.insertBook(book);
        return ResponseResult.conditionResult(i > 0, "添加成功", "添加失败");
    }

    /**
     * 修改图书
     */
    @Put
    public ResponseResult updateBook(@Body Book book) {
        int i = bookService.updateBook(book);
        return ResponseResult.conditionResult(i > 0, "修改成功", "修改失败");
    }

    @Delete("/{id}")
    public ResponseResult delete(@Path("id") int id) {
        int i = bookService.deleteBookById(id);
        return ResponseResult.conditionResult(i > 0, "删除成功", "删除失败");
    }

}
