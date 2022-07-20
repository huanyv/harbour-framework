package top.huanyv.view;

import junit.framework.TestCase;

public class StaticResourceHandlerTest extends TestCase {

    public void testHasResource() {
        StaticResourceHandler resourceHandler = StaticResourceHandler.single();
        resourceHandler.init("static");
        System.out.println("resourceHandler.hasResource(\"/style/index.css\") = "
                + resourceHandler.hasResource("/style/index.css"));

    }
}