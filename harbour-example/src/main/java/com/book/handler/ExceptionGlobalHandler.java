package com.book.handler;

import com.book.pojo.ResponseResult;
import top.huanyv.bean.annotation.Component;
import top.huanyv.webmvc.annotation.ExceptionPoint;
import top.huanyv.webmvc.core.HttpRequest;
import top.huanyv.webmvc.core.HttpResponse;
import top.huanyv.webmvc.exception.ExceptionHandler;

import java.io.IOException;

/**
 * @author admin
 * @date 2022/8/4 17:19
 */
@Component
public class ExceptionGlobalHandler implements ExceptionHandler {

    @ExceptionPoint({IllegalArgumentException.class, NumberFormatException.class})
    public void illegalArgument(HttpRequest request, HttpResponse response, Exception e) throws IOException {
        response.json(ResponseResult.fail("参数不合法"));
    }

    @ExceptionPoint(NullPointerException.class)
    public void nullPointer(HttpRequest request, HttpResponse response, Exception e) throws IOException {
        response.html("<h1>Null Pointer</h1>");
    }

}
