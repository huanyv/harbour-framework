package com.book.handler;

import com.book.pojo.ResponseResult;
import top.huanyv.ioc.anno.Component;
import top.huanyv.jdbc.anno.Mapper;
import top.huanyv.web.anno.ExceptionPoint;
import top.huanyv.web.core.HttpRequest;
import top.huanyv.web.core.HttpResponse;
import top.huanyv.web.exception.ExceptionHandler;

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

}
