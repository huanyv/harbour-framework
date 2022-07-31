package com.book.controller;

import com.book.pojo.Book;
import com.book.pojo.ResponseResult;
import com.book.pojo.vo.BookVo;
import com.book.service.BookService;
import top.huanyv.ioc.anno.Autowired;
import top.huanyv.ioc.anno.Component;
import top.huanyv.utils.BeanUtil;
import top.huanyv.web.anno.*;
import top.huanyv.web.core.HttpRequest;
import top.huanyv.web.core.HttpResponse;


import java.io.IOException;
import java.util.List;

@Component
@Route("/admin/book")
public class BookController {

    @Autowired
    private BookService bookService;

    @Get("/list")
    public void listBook(HttpRequest req, HttpResponse resp) throws IOException {
        List<Book> books = bookService.listBook();
        resp.json(new ResponseResult(0,"", (long) books.size(), books));
    }

    /**
     * 添加图书
     * @return
     */
    @Post("/addBook")
    public void addBook(HttpRequest req, HttpResponse resp) throws IOException {
        ResponseResult ResponseResult = new ResponseResult();
        Book book = BeanUtil.ObjectFromMap(req.getParameterMap(), Book.class);
        Integer i = bookService.insertBook(book);
        if (i > 0) {
            ResponseResult.setCode(ResponseResult.OK);
            ResponseResult.setMsg("添加成功");
        } else {
            ResponseResult.setCode(ResponseResult.SERVER_ERROR);
            ResponseResult.setMsg("添加失败");
        }
        resp.json(ResponseResult);
    }

    /**
     * 修改图书
     * @return
     */
    @Post("/updateBook")
    public void updateBook(HttpRequest req, HttpResponse resp) throws IOException {
        ResponseResult ResponseResult = new ResponseResult();
        Book book = BeanUtil.ObjectFromMap(req.getParameterMap(), Book.class);
        Integer i = bookService.updateBook(book);
        if (i > 0) {
            ResponseResult.setCode(ResponseResult.OK);
            ResponseResult.setMsg("修改成功");
        } else {
            ResponseResult.setCode(ResponseResult.SERVER_ERROR);
            ResponseResult.setMsg("修改失败");
        }
        resp.json(ResponseResult);
    }

    @Post("/deleteBookById")
    public void deleteBook(HttpRequest req, HttpResponse resp) throws IOException {
        String id = req.getParam("id");
        ResponseResult ResponseResult = new ResponseResult();
        Integer i = bookService.deleteBookById(Integer.valueOf(id));
        if (i > 0) {
            ResponseResult.setCode(ResponseResult.OK);
            ResponseResult.setMsg("删除成功");
        } else {
            ResponseResult.setCode(ResponseResult.SERVER_ERROR);
            ResponseResult.setMsg("删除失败");
        }
        resp.json(ResponseResult);
    }


}
