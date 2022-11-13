package top.huanyv.web.view;

import org.junit.Test;
import top.huanyv.tools.utils.IoUtil;
import top.huanyv.webmvc.config.ResourceMappingRegistry;
import top.huanyv.webmvc.view.ResourceHandler;

import java.io.InputStream;

public class ResourceHandlerTest {


    @Test
    public void test01() {
        ResourceHandler resourceHandler = new ResourceHandler();
        ResourceMappingRegistry resourceMappingRegistry = new ResourceMappingRegistry();
        resourceMappingRegistry.addResourceHandler("/**").addResourceLocations("classpath:static/")
                .addResourceLocations("file:D:\\Learn\\notes\\前端\\img\\")
                .addResourceLocations("file:C:\\Users\\admin\\Desktop\\demo\\src\\main\\java\\org\\example\\");
        resourceHandler.setResourceMappingRegistry(resourceMappingRegistry);
        InputStream inputStream = resourceHandler.getInputStream("/error/404.html");
        System.out.println(IoUtil.readStr(inputStream));
    }

}