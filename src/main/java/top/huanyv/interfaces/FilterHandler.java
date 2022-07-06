package top.huanyv.interfaces;

import top.huanyv.core.HttpRequest;
import top.huanyv.core.HttpResponse;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@FunctionalInterface
public interface FilterHandler {
    void handle(HttpRequest req, HttpResponse resp, FilterChain chain) throws IOException, ServletException;
}
