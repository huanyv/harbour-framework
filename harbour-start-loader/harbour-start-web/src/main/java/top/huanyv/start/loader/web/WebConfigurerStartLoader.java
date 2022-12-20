package top.huanyv.start.loader.web;

import top.huanyv.bean.annotation.Bean;
import top.huanyv.bean.ioc.ApplicationContext;
import top.huanyv.bean.utils.BeanFactoryUtil;
import top.huanyv.start.anntation.Conditional;
import top.huanyv.start.config.AppArguments;
import top.huanyv.start.loader.ApplicationLoader;
import top.huanyv.start.loader.Condition;
import top.huanyv.start.web.WebConfiguration;
import top.huanyv.webmvc.config.WebConfigurer;

/**
 * @author huanyv
 * @date 2022/12/20 16:44
 */
public class WebConfigurerStartLoader implements ApplicationLoader {


    @Bean
    @Conditional(ConditionOnMissingBean.class)
    public WebConfigurer webConfigurer() {
        return new WebConfiguration();
    }

    public static class ConditionOnMissingBean implements Condition {

        @Override
        public boolean matchers(ApplicationContext applicationContext, AppArguments appArguments) {
            return BeanFactoryUtil.isNotPresent(applicationContext, WebConfigurer.class);
        }
    }
}
