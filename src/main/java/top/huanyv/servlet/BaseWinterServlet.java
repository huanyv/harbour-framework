package top.huanyv.servlet;

import top.huanyv.interfaces.ServletHandler;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class BaseWinterServlet extends HttpServlet {

    private ServletHandler servletHandler;

    public void setServletHandler(ServletHandler servletHandler) {
        this.servletHandler = servletHandler;
    }

    public void serve(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        servletHandler.handle(req, resp);
    }

}
