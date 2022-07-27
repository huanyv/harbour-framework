package top.huanyv.web.view;

import org.junit.Test;
import top.huanyv.utils.IoUtil;

import java.io.InputStream;

import static org.junit.Assert.*;

public class ResourceHandlerTest {

    @Test
    public void getInputStream() {
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.add("/web/static", "classpath:static, classpath:, C:\\Users\\admin\\Desktop\\cogo");
        InputStream inputStream = resourceHandler.getInputStream("/web/static/error/404.html");
        System.out.println(IoUtil.readStr(inputStream));
        InputStream inputStream1 = resourceHandler.getInputStream("/web/static/style.css");
        System.out.println(IoUtil.readStr(inputStream1));
        InputStream inputStream2 = resourceHandler.getInputStream("/web/static/src/test/java/vip/manda/test/annotation/Test1.java");
        System.out.println(IoUtil.readStr(inputStream2));

    }
}