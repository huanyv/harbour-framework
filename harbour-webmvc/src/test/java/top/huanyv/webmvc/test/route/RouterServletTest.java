package top.huanyv.webmvc.test.route;

import org.junit.Test;
import org.mockito.Mockito;
import top.huanyv.webmvc.core.HttpRequest;
import top.huanyv.webmvc.core.HttpResponse;
import top.huanyv.webmvc.core.RouterServlet;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author huanyv
 * @date 2023/5/14 20:02
 */
public class RouterServletTest {

    @Test
    public void testRoute() throws ServletException, IOException {
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse resp = Mockito.mock(HttpServletResponse.class);

        Servlet servlet = new RouterServlet();
        servlet.service(req, resp);
    }

}
