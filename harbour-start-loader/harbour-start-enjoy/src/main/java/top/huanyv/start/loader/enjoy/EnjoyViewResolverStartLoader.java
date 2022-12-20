package top.huanyv.start.loader.enjoy;

import top.huanyv.bean.annotation.Bean;
import top.huanyv.bean.ioc.ApplicationContext;
import top.huanyv.bean.utils.BeanFactoryUtil;
import top.huanyv.start.anntation.Conditional;
import top.huanyv.start.anntation.ConfigurationProperties;
import top.huanyv.start.config.AppArguments;
import top.huanyv.start.loader.ApplicationLoader;
import top.huanyv.start.loader.Condition;
import top.huanyv.webmvc.view.ViewResolver;
import top.huanyv.webmvc.view.ViewResolverType;
import top.huanyv.webmvc.view.enjoy.EnjoyViewResolver;

import java.nio.charset.StandardCharsets;

/**
 * @author huanyv
 * @date 2022/12/20 17:10
 */
@ConfigurationProperties(prefix = "harbour.view")
public class EnjoyViewResolverStartLoader implements ApplicationLoader {

    private String prefix = "templates/";

    private String suffix = ".html";

    private boolean devMode = true;

    private boolean compress = false;

    @Bean
    @Conditional(ConditionOnMissingBean.class)
    public ViewResolver viewResolver() {
        EnjoyViewResolver viewResolver = new EnjoyViewResolver(ViewResolverType.CLASSLOADER);
        viewResolver.setPrefix(this.prefix);
        viewResolver.setSuffix(this.suffix);
        viewResolver.setDevMode(this.devMode);
        if (this.compress) {
            viewResolver.setCompressorOn('\n');
        }
        return viewResolver;
    }

    public static class ConditionOnMissingBean implements Condition {

        @Override
        public boolean matchers(ApplicationContext applicationContext, AppArguments appArguments) {
            return BeanFactoryUtil.isNotPresent(applicationContext, ViewResolver.class);
        }
    }
}
