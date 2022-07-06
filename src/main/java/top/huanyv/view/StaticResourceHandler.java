package top.huanyv.view;

import cn.hutool.core.io.IoUtil;
import top.huanyv.utils.SystemConstants;
import top.huanyv.utils.WebUtil;

import javax.servlet.ServletOutputStream;
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

    // 资源后缀对应的contentType
    public static final Map<String, String> CONTENT_TYPES = new HashMap<>();

    static {
        CONTENT_TYPES.put(".html", "text/html;charset=utf-8");
        CONTENT_TYPES.put(".css", "text/css");
        CONTENT_TYPES.put(".json", "application/json;charset=utf-8");
        CONTENT_TYPES.put(".js", "text/javascript");
        CONTENT_TYPES.put(".jpg", "image/jpeg");
        CONTENT_TYPES.put(".png", "image/png");
        CONTENT_TYPES.put(".git", "image/git");
        CONTENT_TYPES.put(".ico", "image/x-icon");
    }

    private String prefix;

    /**
     * 设置静态资源前缀
     * @param prefix 前缀
     */
    public void init(String prefix) {
        this.prefix = prefix;
    }

    /**
     * 是否有这个静态资源，如果是 "/" 和 没有后缀，会直接返回false
     * @param name 静态资源名，一般直接用请求地址
     */
    public boolean hasResource(String name) {
        if (name.equals(SystemConstants.PATH_SEPARATOR)) {
            return false;
        }
        if (!WebUtil.hasExtension(name)) {
            return false;
        }
        InputStream inputStream = ClassLoader.getSystemResourceAsStream(this.prefix + name);
        if (inputStream != null) {
            return true;
        }
        return false;
    }

    /**
     * 跳转到静态资源
     * @param uri 请求地址，就是静态资源名，自动拼接上前缀
     */
    public void process(String uri, HttpServletResponse resp) throws IOException {
        // 获取资源后缀
        String extension = WebUtil.getExtension(uri);
        // 设置响应头
        String contentType = CONTENT_TYPES.get(extension);
        resp.setContentType(contentType);

        InputStream inputStream = ClassLoader.getSystemResourceAsStream(this.prefix + uri);
        ServletOutputStream outputStream = resp.getOutputStream();

//        int len = 0;
//        byte[] buffer = new byte[4096];
//        while ((len = inputStream.read(buffer)) != -1) {
//            outputStream.write(buffer, 0, len);
//        }
//
//        outputStream.flush();
//        inputStream.close();
//        outputStream.close();
        IoUtil.copy(inputStream, outputStream);
    }

}
