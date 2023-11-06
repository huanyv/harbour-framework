package top.huanyv.admin.config;

import top.huanyv.bean.annotation.Bean;
import top.huanyv.webmvc.annotation.ExceptionPoint;
import top.huanyv.webmvc.core.HttpRequest;
import top.huanyv.webmvc.core.HttpResponse;
import top.huanyv.webmvc.exception.ExceptionHandler;

import java.io.IOException;

/**
 * @author huanyv
 * @date 2023/5/18 23:54
 */
@Bean
public class GlobalExceptionHandler implements ExceptionHandler {

    @ExceptionPoint({NullPointerException.class})
    public void nullExhandler(HttpRequest request, HttpResponse response, Exception e) throws IOException {
        System.out.println("空指针异常");
        response.error(Integer.parseInt(e.getMessage()));
    }

}
