package top.huanyv.start.loader.web;

import top.huanyv.bean.annotation.Bean;
import top.huanyv.bean.annotation.Order;
import top.huanyv.bean.ioc.ApplicationContext;
import top.huanyv.bean.utils.BeanFactoryUtil;
import top.huanyv.start.anntation.Conditional;
import top.huanyv.start.config.AppArguments;
import top.huanyv.start.loader.ApplicationLoader;
import top.huanyv.start.loader.Condition;
import top.huanyv.webmvc.core.RouterServlet;

/**
 * @author huanyv
 * @date 2022/12/20 15:26
 */
@Order(-10)
public class RouterServletStartLoader implements ApplicationLoader {

    private ApplicationContext applicationContext;

    @Override
    public void load(ApplicationContext applicationContext, AppArguments appArguments) {
        this.applicationContext = applicationContext;
    }

    @Bean
    @Conditional(ConditionOnMissingBean.class)
    public RouterServlet routerServlet() {
        return new RouterServlet(applicationContext);
    }

    public static class ConditionOnMissingBean implements Condition {

        @Override
        public boolean matchers(ApplicationContext applicationContext, AppArguments appArguments) {
            return BeanFactoryUtil.isNotPresent(applicationContext, RouterServlet.class);
        }
    }
}
