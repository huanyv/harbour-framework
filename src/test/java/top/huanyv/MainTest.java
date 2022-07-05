package top.huanyv;

import jdk.nashorn.internal.ir.ReturnNode;
import org.junit.Test;
import top.huanyv.core.RequestHandlerRegistry;
import top.huanyv.core.Winter;

import java.io.File;
import java.net.URL;
import java.util.Arrays;

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

        app.request("/admin/{id}/{name}", (req, resp) -> {
            return "html:<h1>success</h1>";
        });

        app.start();
    }

    @Test
    public void test02() {
        String str = "/admin/get/{id}";
        String[] paths = str.split("/");
        System.out.println("Arrays.toString(paths) = " + Arrays.toString(paths));
        System.out.println(compareUrl("/admin/1/zhangsan", "/admin/{id}/{name}"));

    }

    public boolean compareUrl(String s1, String s2) {
        String[] s1Arr = s1.split("/");
        String[] s2Arr = s2.split("/");
        if (s1Arr.length != s2Arr.length) {
            return false;
        }
        for (int i = 0; i < s1Arr.length; i++) {
            if (s2Arr[i].matches("\\{[0-9a-zA-Z]+\\}")){
                continue;
            }
            if (!s1Arr[i].equals(s2Arr[i])) {
                return false;
            }
        }
        return true;
    }
}

