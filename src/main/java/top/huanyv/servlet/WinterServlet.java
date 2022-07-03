package top.huanyv.servlet;

import top.huanyv.enums.RequestMethod;
import top.huanyv.interfaces.ServletHandler;
import top.huanyv.utils.SystemConstants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class WinterServlet extends HttpServlet {

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
                String responseInfo = servletHandler.handle(req, resp);

                if (responseInfo.startsWith(SystemConstants.RESPONSE_FORWARD_PREFIX)) {
                    responseInfo = responseInfo.substring(SystemConstants.RESPONSE_FORWARD_PREFIX.length());
                    req.getRequestDispatcher(responseInfo).forward(req, resp);
                } else if (responseInfo.startsWith(SystemConstants.RESPONSE_REDIRECT_PREFIX)) {
                    responseInfo = responseInfo.substring(SystemConstants.RESPONSE_REDIRECT_PREFIX.length());
                    resp.sendRedirect(req.getContextPath() + responseInfo);
                } else if (responseInfo.startsWith(SystemConstants.RESPONSE_JSON_PREFIX)) {
                    responseInfo = responseInfo.substring(SystemConstants.RESPONSE_REDIRECT_PREFIX.length());
                    resp.setContentType("application/json");
                    resp.getWriter().println(responseInfo);
                } else  if (responseInfo.startsWith(SystemConstants.RESPONSE_HTML_PREFIX)) {
                    responseInfo = responseInfo.substring(SystemConstants.RESPONSE_HTML_PREFIX.length());
                    resp.setContentType("text/html");
                    resp.getWriter().println(responseInfo);
                } else  if (responseInfo.startsWith(SystemConstants.RESPONSE_TEXT_PREFIX)) {
                    responseInfo = responseInfo.substring(SystemConstants.RESPONSE_TEXT_PREFIX.length());
                    resp.setContentType("text/plain");
                    resp.getWriter().println(responseInfo);
                } else {
                    resp.setContentType("text/html");
                    resp.getWriter().println(responseInfo);
                }

                return;
            }
        }
        resp.setStatus(405);
        resp.getWriter().println("<h1>405</h1>");
    }
}
