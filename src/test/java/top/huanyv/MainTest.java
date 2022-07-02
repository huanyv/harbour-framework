package top.huanyv;

import org.junit.Test;
import top.huanyv.core.Winter;

public class MainTest {
    @Test
    public void test() {
        Winter app = Winter.getInstance();
        app.init(8090);
        app.get("/admin", (req, resp) -> {
            resp.getWriter().println("<h1>Hello Winter!!!!</h1>");
        });

        app.start();
    }
}
