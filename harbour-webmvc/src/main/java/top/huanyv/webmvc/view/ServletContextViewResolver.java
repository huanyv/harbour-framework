package top.huanyv.webmvc.view;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * servlet上下文jsp视图解析器
 *
 * @author huanyv
 * @date 2022/11/7 16:56
 */
public class ServletContextViewResolver implements ViewResolver {

    private String prefix;

    private String suffix;

    @Override
    public void render(String templateName, HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        req.getRequestDispatcher(prefix + templateName + suffix).forward(req, resp);
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
}
