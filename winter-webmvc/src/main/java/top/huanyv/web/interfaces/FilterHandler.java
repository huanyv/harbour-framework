package top.huanyv.web.interfaces;

import top.huanyv.web.core.HttpRequest;
import top.huanyv.web.core.HttpResponse;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import java.io.IOException;

@FunctionalInterface
public interface FilterHandler {
    void handle(HttpRequest req, HttpResponse resp, FilterChain chain) throws IOException, ServletException;
}
