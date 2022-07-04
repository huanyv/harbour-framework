package top.huanyv.servlet;

import top.huanyv.core.RequestHandlerRegistry;
import top.huanyv.enums.RequestMethod;
import top.huanyv.interfaces.ServletHandler;
import top.huanyv.utils.SystemConstants;
import top.huanyv.utils.WebUtil;
import top.huanyv.view.StaticResourceHandler;
import top.huanyv.view.TemplateEngineInstance;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.EndpointReference;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class WinterServlet extends HttpServlet {

    /**
     * 请求注册容器
     */
    private RequestHandlerRegistry registry;

    private TemplateEngineInstance templateEngineInstance;

    @Override
    public void init() throws ServletException {
        registry = RequestHandlerRegistry.single();
        templateEngineInstance = TemplateEngineInstance.single();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = WebUtil.getRequestURI(req);

        if (!uri.equals("/")) {
            // 处理静态资源
            boolean handle = staticHandle(uri, resp);
            if (handle) {
                return;
            }
        }

        Map<RequestMethod, ServletHandler> requestHandler = this.registry.getHandler(uri);
        for (Map.Entry<RequestMethod, ServletHandler> handlerEntry : requestHandler.entrySet()) {
            RequestMethod method = handlerEntry.getKey();
            ServletHandler handler = handlerEntry.getValue();
            if (method.name().equals(req.getMethod()) && handler != null) {
                String responseInfo = handler.handle(req, resp);

                responseHandle(req, resp, responseInfo);

                return;
            }

        }

        if (this.registry.containsRequest(uri)) {
            resp.setStatus(405);
            resp.getWriter().println("<h1>405</h1>");
            return;
        }

        resp.setStatus(404);
        resp.getWriter().println("<h1>404</h1>");
    }

    public boolean staticHandle(String uri, HttpServletResponse resp) throws IOException {
        StaticResourceHandler resourceHandler = StaticResourceHandler.single();
        if (resourceHandler.hasResource(uri)) {
            resourceHandler.process(uri, resp);
            return true;
        }
        return false;
    }

    public void responseHandle(HttpServletRequest req, HttpServletResponse resp, String responseInfo) throws ServletException, IOException {
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
        } else if(responseInfo.startsWith(SystemConstants.RESPONSE_TH_PREFIX)) {
            responseInfo = responseInfo.substring(SystemConstants.RESPONSE_TH_PREFIX.length());
            templateEngineInstance.process(responseInfo, req, resp);
        }else {
            resp.setContentType("text/html");
            resp.getWriter().println(responseInfo);
        }
    }

}
