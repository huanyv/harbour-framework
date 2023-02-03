package top.huanyv.start.loader.thymeleaf;

import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import top.huanyv.bean.annotation.Bean;
import top.huanyv.bean.ioc.ApplicationContext;
import top.huanyv.bean.utils.BeanFactoryUtil;
import top.huanyv.start.anntation.Conditional;
import top.huanyv.start.anntation.Properties;
import top.huanyv.start.config.AppArguments;
import top.huanyv.start.loader.ApplicationLoader;
import top.huanyv.start.loader.Condition;
import top.huanyv.webmvc.view.ViewResolver;
import top.huanyv.webmvc.view.thymeleaf.ThymeleafViewResolver;

import java.nio.charset.StandardCharsets;

/**
 * @author huanyv
 * @date 2022/12/18 14:55
 */
@Properties(prefix = "harbour.view.")
public class ThymeleafViewResolverStartLoader implements ApplicationLoader {

    private String prefix = "templates/";

    private String suffix = ".html";

    private String encoding = StandardCharsets.UTF_8.name();

    @Bean
    @Conditional(ConditionOnMissingBean.class)
    public ViewResolver viewResolver() {
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver(new ClassLoaderTemplateResolver());
        viewResolver.setPrefix(this.prefix);
        viewResolver.setSuffix(this.suffix);
        viewResolver.setCharacterEncoding(this.encoding);
        viewResolver.setTemplateMode(TemplateMode.HTML);
        return viewResolver;
    }

    public static class ConditionOnMissingBean implements Condition {

        @Override
        public boolean matchers(ApplicationContext applicationContext, AppArguments appArguments) {
            return BeanFactoryUtil.isNotPresent(applicationContext, ViewResolver.class);
        }
    }

}
