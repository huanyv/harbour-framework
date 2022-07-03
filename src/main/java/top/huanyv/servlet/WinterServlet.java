package top.huanyv.servlet;

import top.huanyv.enums.RequestMethod;
import top.huanyv.interfaces.ServletHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class WinterServlet extends BaseWinterServlet{

    /**
     * 每个请求的处理器，GET,post,put,delete
     */
    Map<RequestMethod, ServletHandler> requestHandler = new HashMap<>();

    public void setRequestHandler(Map<RequestMethod, ServletHandler> requestHandler) {
        this.requestHandler = requestHandler;
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        for (Map.Entry<RequestMethod, ServletHandler> entry : this.requestHandler.entrySet()) {
            RequestMethod method = entry.getKey();
            ServletHandler servletHandler = entry.getValue();
            if (method.getName().equalsIgnoreCase(req.getMethod())) {
                servletHandler.handle(req, resp);

                return;
            }
        }
        resp.setStatus(405);
        resp.getWriter().println("<h1>405</h1>");
    }
}
