package top.huanyv.start.config;

import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import top.huanyv.bean.annotation.Bean;
import top.huanyv.bean.annotation.Configuration;
import top.huanyv.webmvc.view.ViewResolver;
import top.huanyv.webmvc.view.thymeleaf.ThymeleafViewResolver;

import java.nio.charset.StandardCharsets;

/**
 * @author admin
 * @date 2022/7/29 16:54
 */
@Configuration
public class BootWebConfiguration {

    @Bean
    public ViewResolver viewResolver() {
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver(new ClassLoaderTemplateResolver());
        viewResolver.setPrefix("templates/");
        viewResolver.setSuffix(".html");
        viewResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
        viewResolver.setTemplateMode(TemplateMode.HTML);
        return viewResolver;
    }

}
