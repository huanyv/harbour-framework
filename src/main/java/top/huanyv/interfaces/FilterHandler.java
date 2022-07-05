package top.huanyv.interfaces;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@FunctionalInterface
public interface FilterHandler {
    void handle(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws IOException, ServletException;
}
