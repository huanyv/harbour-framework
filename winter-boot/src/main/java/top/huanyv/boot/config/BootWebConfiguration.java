package top.huanyv.boot.config;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import top.huanyv.ioc.anno.Bean;
import top.huanyv.ioc.anno.Configuration;
import top.huanyv.web.view.ThymeleafViewResolver;
import top.huanyv.web.view.ViewResolver;

import java.nio.charset.StandardCharsets;

/**
 * @author admin
 * @date 2022/7/29 16:54
 */
@Configuration
public class BootWebConfiguration {

    @Bean
    public ViewResolver viewResolver() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
        templateResolver.setTemplateMode(TemplateMode.HTML);
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        ThymeleafViewResolver thymeleafViewResolver = new ThymeleafViewResolver();
        thymeleafViewResolver.setTemplateEngine(templateEngine);
        return thymeleafViewResolver;
    }

}
