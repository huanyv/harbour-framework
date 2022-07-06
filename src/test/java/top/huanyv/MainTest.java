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
        Winter app = Winter.use();
        app.get("/admin", (req, resp) -> {
            resp.html("<h1>admin req get</h1>");
        });

        app.post("/admin", (req, resp) -> {
            resp.html("<h1>admin post</h1>");
        });

        app.request("/hello", (req, resp) -> {
            resp.html("<h1>helloooooooo</h1>");
            System.out.println("req.body() = " + req.body());
        });

        app.request("/admin/{id}/{name}", (req, resp) -> {
            resp.html("<h1>path</h1>");
            System.out.println("req.pathVar(\"id\") = " + req.pathVar("id"));
            System.out.println("req.pathVar(\"name\") = " + req.pathVar("name"));
        });

        app.filter("/hello", (req, resp, chain) -> {
            System.out.println("hello test");
            chain.doFilter(req.getOriginal(), resp.getOriginal());
        });

        app.filter("/hello", (req, resp, chain) -> {
            System.out.println("admin, filter");
            chain.doFilter(req.getOriginal(), resp.getOriginal());
        });

        app.post("/upload", (req, resp) -> {
            req.uploadFile(new File("C:\\Users\\admin\\Desktop\\新建文件夹"));
        });

        app.get("/down", (req, resp) -> {
            resp.file(new File("C:\\Users\\admin\\Desktop\\新建文件夹\\myblogadmin.zip"));
        });

        app.init(8090, "/this");

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

