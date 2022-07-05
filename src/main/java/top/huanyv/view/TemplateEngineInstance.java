package top.huanyv.view;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TemplateEngineInstance {

    private TemplateEngineInstance() {
    }

    private static class SingletonHolder {
        private static final TemplateEngineInstance INSTANCE = new TemplateEngineInstance();
    }

    public static TemplateEngineInstance single() {
        return SingletonHolder.INSTANCE;
    }

    private TemplateEngine templateEngine;
    private ClassLoaderTemplateResolver templateResolver;

    private String prefix;
    private String suffix;

    public void init(String prefix, String suffix) {
        init(prefix, suffix, StandardCharsets.UTF_8.name());
    }

    public void init(String prefix, String suffix, String encoding) {
        if (prefix.endsWith("/")) {
            this.prefix = prefix;
        } else {
            this.prefix = prefix + "/";
        }
        this.suffix = suffix;

        templateResolver = new ClassLoaderTemplateResolver(ClassLoader.getSystemClassLoader());
        templateResolver.setPrefix(this.prefix);
        templateResolver.setSuffix(this.suffix);

        templateResolver.setCacheTTLMs(60000L);
        templateResolver.setCacheable(true);
        templateResolver.setCharacterEncoding(encoding);

        templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
    }

    public void process(String templateName, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // 1.设置响应体内容类型和字符集
        resp.setContentType("text/html");
        // 2.创建WebContext对象
        WebContext webContext = new WebContext(req, resp, req.getServletContext());
        // 3.处理模板数据
        templateEngine.process(templateName, webContext, resp.getWriter());
    }




}
