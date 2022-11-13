package top.huanyv.boot.test.config;

import com.mysql.jdbc.Driver;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import top.huanyv.bean.annotation.Bean;
import top.huanyv.bean.annotation.Component;
import top.huanyv.bean.annotation.Configuration;
import top.huanyv.webmvc.config.WebConfigurer;
import top.huanyv.webmvc.view.ThymeleafViewResolver;
import top.huanyv.webmvc.view.ViewResolver;

import javax.sql.DataSource;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Properties;

/**
 * @author admin
 * @date 2022/7/29 16:12
 */
@Configuration
@Component
public class WebConfig implements WebConfigurer {

//    @Bean
//    public ViewResolver viewResolver() {
//        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
//        templateResolver.setPrefix("templates/");
//        templateResolver.setSuffix(".html");
//        templateResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
//        templateResolver.setTemplateMode(TemplateMode.HTML);
//        TemplateEngine templateEngine = new TemplateEngine();
//        templateEngine.setTemplateResolver(templateResolver);
//        ThymeleafViewResolver thymeleafViewResolver = new ThymeleafViewResolver();
//        thymeleafViewResolver.setTemplateEngine(templateEngine);
//        return thymeleafViewResolver;
//    }

//    @Bean
//    public DataSource dataSource() {
//        SimpleDataSource simpleDataSource = new SimpleDataSource();
//        simpleDataSource.setUrl("jdbc:mysql://localhost:3306/temp?useSSL=false");
//        simpleDataSource.setUsername("root");
//        simpleDataSource.setPassword("2233");
//        simpleDataSource.setDriverClassName(Driver.class.getName());
//        return simpleDataSource;
//    }

}
