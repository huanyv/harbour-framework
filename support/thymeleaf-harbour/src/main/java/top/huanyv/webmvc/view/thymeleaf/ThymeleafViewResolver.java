package top.huanyv.webmvc.view.thymeleaf;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.*;
import top.huanyv.webmvc.utils.ServletHolder;
import top.huanyv.webmvc.view.ViewResolver;
import top.huanyv.webmvc.view.ViewResolverType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * thymeleaf视图解析器
 *
 * @author admin
 * @date 2022/7/25 16:51
 */
public class ThymeleafViewResolver implements ViewResolver {

    private TemplateEngine templateEngine;

    public void setTemplateEngine(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    private AbstractConfigurableTemplateResolver thymeleafResolver;

    public ThymeleafViewResolver(ViewResolverType type) {
        switch (type) {
            case CLASSLOADER:
                thymeleafResolver = new ClassLoaderTemplateResolver();
                break;
            case SERVLET_CONTEXT:
                thymeleafResolver = new ServletContextTemplateResolver(ServletHolder.getServletContext());
                break;
            case FILE:
                thymeleafResolver = new FileTemplateResolver();
                break;
            case URL:
                thymeleafResolver = new UrlTemplateResolver();
                break;
        }
        this.templateEngine = new TemplateEngine();
        this.templateEngine.setTemplateResolver(this.thymeleafResolver);
    }
    public ThymeleafViewResolver(AbstractConfigurableTemplateResolver resolver) {
        this.thymeleafResolver = resolver;
        this.templateEngine = new TemplateEngine();
        this.templateEngine.setTemplateResolver(this.thymeleafResolver);
    }

    public void setPrefix(String prefix) {
        this.thymeleafResolver.setPrefix(prefix);
    }

    public void setSuffix(String suffix) {
        this.thymeleafResolver.setSuffix(suffix);
    }

    public void setCharacterEncoding(String characterEncoding) {
        this.thymeleafResolver.setCharacterEncoding(characterEncoding);
    }

    public void setTemplateMode(TemplateMode templateMode) {
        this.thymeleafResolver.setTemplateMode(templateMode);
    }

    public void render(String templateName, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // 1.设置响应体内容类型和字符集
        resp.setContentType("text/html");
        // 2.创建WebContext对象
        WebContext webContext = new WebContext(req, resp, req.getServletContext());
        // 3.处理模板数据
        templateEngine.process(templateName, webContext, resp.getWriter());
    }
}
