package top.huanyv.web.view;

import org.junit.Test;
import top.huanyv.utils.IoUtil;

import java.io.InputStream;

public class ResourceHandlerTest {


    @Test
    public void test01() {
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.add("/**").addResourceLocations("classpath:static/")
                .addResourceLocations("D:\\Learn\\notes\\前端\\img\\")
                .addResourceLocations("C:\\Users\\admin\\Desktop\\demo\\src\\main\\java\\org\\example\\");
        InputStream inputStream = resourceHandler.getInputStream("/config/Webconfig.java");
        System.out.println(IoUtil.readStr(inputStream));
    }

    @Test
    public void addMapping() {
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.add("/**").addResourceLocations("classpath:static/")
                .addResourceLocations("D:\\Learn\\notes\\前端\\img\\")
                .addResourceLocations("C:\\Users\\admin\\Desktop\\demo\\src\\main\\java\\org\\example\\");
        ResourceMapping resourceMapping = new ResourceMapping();
        resourceMapping.setUrlPattern("/static/**");
        resourceMapping.addResourceLocations("/var/www/ccc/");

        resourceHandler.addMapping(resourceMapping);

        System.out.println(resourceHandler);

    }
}