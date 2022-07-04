package top.huanyv.view;

import top.huanyv.utils.WebUtil;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class StaticResourceHandler {

    // 单例
    private StaticResourceHandler() {}
    private static class SingletonHolder {
        private static final StaticResourceHandler INSTANCE = new StaticResourceHandler();
    }
    public static StaticResourceHandler single() {
        return SingletonHolder.INSTANCE;
    }

    public static final Map<String, String> CONTENT_TYPES = new HashMap<>();

    static {
        CONTENT_TYPES.put(".html", "text/html;charset=utf-8");
        CONTENT_TYPES.put(".css", "text/css");
        CONTENT_TYPES.put(".json", "application/json;charset=utf-8");
        CONTENT_TYPES.put(".js", "text/javascript");
        CONTENT_TYPES.put(".jpg", "image/jpeg");
        CONTENT_TYPES.put(".png", "image/png");
        CONTENT_TYPES.put(".ico", "image/x-icon");
    }

    private String prefix;

    private String uri;

    public void init(String prefix) {
        this.prefix = prefix;
    }

    public boolean hasResource(String name) {
        InputStream inputStream = ClassLoader.getSystemResourceAsStream(this.prefix + name);
        if (inputStream != null) {
            return true;
        }
        return false;
    }


    public void process(String uri, HttpServletResponse resp) throws IOException {
        String extension = WebUtil.getExtension(uri);
        String contentType = CONTENT_TYPES.get(extension);
        resp.setContentType(contentType);

        InputStream inputStream = ClassLoader.getSystemResourceAsStream(this.prefix + uri);
        ServletOutputStream outputStream = resp.getOutputStream();

        int len = 0;
        byte[] buffer = new byte[4096];
        while ((len = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }

        outputStream.flush();
        inputStream.close();
        outputStream.close();
    }

}
