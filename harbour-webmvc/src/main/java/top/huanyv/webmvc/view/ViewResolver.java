package top.huanyv.webmvc.view;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author admin
 * @date 2022/7/25 16:52
 */
public interface ViewResolver {

    void render(String templateName, HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException;

}
