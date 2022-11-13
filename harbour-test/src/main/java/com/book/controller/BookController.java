package com.book.controller;

import com.book.aspect.LogAop;
import com.book.pojo.Book;
import com.book.pojo.ResponseResult;
import com.book.service.BookService;

import top.huanyv.bean.annotation.Component;
import top.huanyv.bean.annotation.Inject;
import top.huanyv.bean.annotation.Scope;
import top.huanyv.bean.aop.Aop;
import top.huanyv.bean.ioc.definition.BeanDefinition;
import top.huanyv.jdbc.core.Page;
import top.huanyv.tools.utils.JsonUtil;
import top.huanyv.webmvc.annotation.*;
import top.huanyv.webmvc.core.HttpRequest;
import top.huanyv.webmvc.core.HttpResponse;


import java.io.IOException;

@Component
@Route("/admin/book")
@Aop(LogAop.class)
//@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class BookController {

    @Inject
    private BookService bookService;

    @Get
    public void listBook(HttpRequest req, HttpResponse resp) throws IOException {

        // 异常处理器测试
//        Object o = null;
//        System.out.println(o.toString());

        String bname = req.getParam("bname");
        int pageNum = Integer.parseInt(req.getParam("page"));
        int pageSize = Integer.parseInt(req.getParam("limit"));
        Page<Book> bookPage = bookService.listBook(bname, pageNum, pageSize);
        resp.json(new ResponseResult(0,"", bookPage.getTotal(), bookPage.getData()));
    }

    /**
     * 添加图书
     */
    @Post
    public void addBook(HttpRequest req, HttpResponse resp) throws IOException {
        Book book = JsonUtil.fromJson(req.body(), Book.class);
        int i = bookService.insertBook(book);
        resp.json(ResponseResult.conditionResult(i > 0, "添加成功", "添加失败"));
    }

    /**
     * 修改图书
     */
    @Put
    public void updateBook(HttpRequest req, HttpResponse resp) throws IOException {
        Book book = JsonUtil.fromJson(req.body(), Book.class);
        int i = bookService.updateBook(book);
        resp.json(ResponseResult.conditionResult(i > 0, "修改成功", "修改失败"));
    }

    @Delete("/{id}")
    public void delete(HttpRequest req, HttpResponse resp) throws IOException {
        String id = req.pathVar("id");
        int i = bookService.deleteBookById(Integer.valueOf(id));
        resp.json(ResponseResult.conditionResult(i > 0, "删除成功", "删除失败"));
    }

}
