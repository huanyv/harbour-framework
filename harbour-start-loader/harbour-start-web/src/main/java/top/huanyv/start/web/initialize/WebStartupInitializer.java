package top.huanyv.start.web.initialize;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * @author huanyv
 * @date 2022/12/24 16:36
 */
public interface WebStartupInitializer {
    void onStartup(ServletContext ctx) throws ServletException;
}
