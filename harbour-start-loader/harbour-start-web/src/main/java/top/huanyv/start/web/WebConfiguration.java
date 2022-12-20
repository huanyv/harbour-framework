package top.huanyv.start.web;

import top.huanyv.bean.ioc.ApplicationContext;
import top.huanyv.bean.utils.BeanFactoryUtil;
import top.huanyv.start.config.AppArguments;
import top.huanyv.start.loader.Condition;
import top.huanyv.webmvc.config.ResourceMappingRegistry;
import top.huanyv.webmvc.config.WebConfigurer;
import top.huanyv.webmvc.core.RouterServlet;

/**
 * @author huanyv
 * @date 2022/12/20 16:45
 */
public class WebConfiguration implements WebConfigurer {

    @Override
    public void addResourceMapping(ResourceMappingRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:static/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:META-INF/resources/webjars/");
    }

}
