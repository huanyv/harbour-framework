package top.huanyv.start.web;

import top.huanyv.webmvc.config.ResourceMappingRegistry;
import top.huanyv.webmvc.config.WebConfigurer;

/**
 * @author huanyv
 * @date 2022/12/20 16:45
 */
public class WebConfiguration implements WebConfigurer {

    @Override
    public void addResourceMapping(ResourceMappingRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:static/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:META-INF/resources/webjars/");
    }

}
