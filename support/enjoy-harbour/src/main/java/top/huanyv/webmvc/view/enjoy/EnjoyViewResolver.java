package top.huanyv.webmvc.view.enjoy;

import com.jfinal.template.Engine;
import com.jfinal.template.Template;
import com.jfinal.template.ext.spring.JFinalView;
import com.jfinal.template.source.FileSourceFactory;
import top.huanyv.webmvc.utils.ServletHolder;
import top.huanyv.webmvc.view.ViewResolver;
import top.huanyv.webmvc.view.ViewResolverType;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Enjoy视图解析器<br />
 * 使用文档：https://jfinal.com/doc/6-3
 *
 * @author huanyv
 * @date 2022/12/12 16:13
 */
public class EnjoyViewResolver implements ViewResolver {

    public static final String SESSION_NAME = "session";

    public static final String CONTEXT_PATH_NAME = "ctxPath";

    private String prefix;
    private String suffix;

    private ViewResolverType viewResolverType;

    Engine engine = Engine.use();

    public EnjoyViewResolver(ViewResolverType viewResolverType) {
        this.viewResolverType = viewResolverType;
        Engine.setFastMode(true);
        this.engine.setDevMode(true);
        switch (viewResolverType) {
            case SERVLET_CONTEXT:
                this.engine.setBaseTemplatePath(ServletHolder.getServletContext().getRealPath("/"));
                break;
            case CLASSLOADER:
                this.engine.setToClassPathSourceFactory();
                this.engine.setBaseTemplatePath(null);
                break;
            case FILE:
                this.engine.setSourceFactory(new FileSourceFactory());
                break;
            default:
                throw new IllegalArgumentException("Not supported template type " + viewResolverType);
        }
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public void setFileTypePath(String path) {
        if (ViewResolverType.FILE.equals(this.viewResolverType)) {
            this.engine.setBaseTemplatePath(path);
        }
    }

    public void setDevMode(boolean devMode) {
        this.engine.setDevMode(devMode);
    }

    public void setCompressorOn(char c) {
        this.engine.setCompressorOn(c);
    }

    @Override
    public void render(String templateName, HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        Map<String, Object> model = new HashMap<>();

        // 设置请求域的值
        Enumeration<String> reqAttributeNames = req.getAttributeNames();
        while (reqAttributeNames.hasMoreElements()) {
            String name = reqAttributeNames.nextElement();
            model.put(name, req.getAttribute(name));
        }

        // 设置session域值
        HttpSession session = req.getSession();
        model.put(SESSION_NAME, new JFinalView.InnerSession(session));

        // 设置 context path 值
        model.put(CONTEXT_PATH_NAME, req.getContextPath());

        Template template = this.engine.getTemplate(prefix + templateName + suffix);

        resp.setContentType("text/html");
        template.render(model, resp.getOutputStream());
    }
}
