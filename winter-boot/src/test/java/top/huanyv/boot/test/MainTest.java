package top.huanyv.boot.test;

import org.junit.Test;
import top.huanyv.boot.core.BootWinter;

public class MainTest {
    @Test
    public void test01() {
        BootWinter app = BootWinter.use();

        app.get("/admin", (req, resp) -> {
            resp.html("<h1>admin</h1>");
        });
        app.get("/", (req, resp) -> {
            req.view("index");
        });

        app.start(MainTest.class, new String[]{});

    }
}

