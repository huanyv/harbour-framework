package top.huanyv;

import org.junit.Test;
import top.huanyv.core.Winter;

public class MainTest {
    @Test
    public void test() {
        Winter app = Winter.getInstance();
        app.init(8090);
        app.get("/admin", (req, resp) -> {
            return "html:<h1>Hello Winter!!!! GET</h1>";
        });

        app.post("/admin", (req, resp) -> {
            return "html:<h1>Hello Winter!!!! POST</h1>";
        });

        app.request("/hello", (req, resp) -> {
            return "<h1>Hello</h1>";
        });

        app.start();
    }
}
