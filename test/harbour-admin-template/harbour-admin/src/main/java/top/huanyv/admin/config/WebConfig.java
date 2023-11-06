package top.huanyv.admin.config;

import com.wf.captcha.servlet.CaptchaServlet;
import top.huanyv.admin.guard.AuthenticationGuard;
import top.huanyv.bean.annotation.Bean;
import top.huanyv.bean.annotation.Bean;
import top.huanyv.bean.ioc.Configuration;
import top.huanyv.start.server.servlet.ServletBean;
import top.huanyv.start.web.WebConfiguration;
import top.huanyv.webmvc.config.CorsRegistry;
import top.huanyv.webmvc.config.NavigationGuardRegistry;
import top.huanyv.webmvc.config.ResourceMappingRegistry;
import top.huanyv.webmvc.config.ViewControllerRegistry;
import top.huanyv.webmvc.security.*;

/**
 * @author huanyv
 * @date 2023/2/21 19:59
 */
@Bean
public class WebConfig extends WebConfiguration implements Configuration {


    @Override
    public void addResourceMapping(ResourceMappingRegistry registry) {
        super.addResourceMapping(registry);
    }

    @Override
    public void addViewController(ViewControllerRegistry registry) {
        registry.add("/", "index");
        registry.add("/login", "login");
        registry.add("/system/user", "system/sysuser/sysuser");
        registry.add("/system/role", "system/sysrole/sysrole");
        registry.add("/system/menu", "system/sysmenu/sysmenu");
        registry.add("/system/dept", "system/sysdept/sysdept");
        registry.add("/system/notice", "system/sysnotice/sysnotice");

        registry.add("/monitor/ioc", "monitor/ioc");
        registry.add("/monitor/log/login", "monitor/log/login");
        registry.add("/monitor/log/oper", "monitor/log/oper");
        registry.add("/monitor/cache", "monitor/cache");
        registry.add("/monitor/route", "monitor/route");
    }

    @Override
    public void configNavigationRegistry(NavigationGuardRegistry registry) {
        registry.addNavigationGuard(new AuthenticationGuard()).addUrlPattern("/**")
                .excludeUrlPattern("/login", "/static/**", "/admin/login");
        registry.addNavigationGuard(new Authorizer()).addUrlPattern("/**");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {

    }

    @Bean
    public ServletBean captchaServletBean() {
        return new ServletBean(new CaptchaServlet(), "/captcha");
    }

}
