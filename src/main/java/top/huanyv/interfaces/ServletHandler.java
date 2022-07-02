package top.huanyv.interfaces;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@FunctionalInterface
public interface ServletHandler {
    void handle(HttpServletRequest req, HttpServletResponse resp) throws IOException;
}
