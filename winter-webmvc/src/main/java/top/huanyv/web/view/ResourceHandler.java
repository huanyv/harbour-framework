package top.huanyv.web.view;

import top.huanyv.enums.MimeTypeEnum;
import top.huanyv.utils.*;
import top.huanyv.web.utils.SystemConstants;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @author admin
 * @date 2022/7/27 8:34
 */
public class ResourceHandler {

    public static final String RESOURCE_LOCATION_PREFIX = "classpath:";

    private Map<String, String> resourceMapping = new HashMap<>();

    // 默认的静态资源前缀
    public ResourceHandler() {
        this.add("/static", "classpath:static");
    }

    /**
     * 获取精确匹配的静态资源前缀
     * @param uri uri
     * @return 前缀 没有为null
     */
    public String getMatchPrefix(String uri) {
        // TODO fix 应该匹配多个urlpattern
        List<String> pres = new ArrayList<>();
        // 遍历所有uri符合的前缀
        for (Map.Entry<String, String> entry : resourceMapping.entrySet()) {
            String prefix = entry.getKey();
            if (uri.startsWith(prefix)) {
                pres.add(prefix);
            }
        }
        // 取出最长的一个，以精确匹配
        Optional<String> max = pres.stream().max((o1, o2) -> o1.length() - o2.length());
        return max.orElse(null);
    }

    public InputStream getInputStream(HttpServletRequest req) {
        String uri = WebUtil.getRequestURI(req);
        InputStream inputStream = req.getServletContext().getResourceAsStream(uri);
        if (inputStream == null) {
            inputStream = getInputStream(WebUtil.getRequestURI(req));
        }
        return inputStream;
    }
    public InputStream getInputStream(String uri) {
        // 获取精确匹配的资源前缀
        String prefix = getMatchPrefix(uri);
        if (prefix == null) {
            return null;
        }
        InputStream inputStream = null;
        // 获取所有的路径
        String[] locations = this.resourceMapping.get(prefix).split(",");
        // 遍历路径, location + uri去掉前缀
        for (int i = 0; i < locations.length; i++) {
            // uri去掉前缀
            String resourcePath = uri.substring(prefix.length());
            // 某个location
            String location = locations[i].trim();
            // 如果是classpath下的
            if (location.startsWith(RESOURCE_LOCATION_PREFIX)) {
                String realLocation = location.substring(RESOURCE_LOCATION_PREFIX.length()) + resourcePath;
                inputStream = ClassLoaderUtil
                        .getInputStream(StringUtil.removePrefix(realLocation, SystemConstants.PATH_SEPARATOR));
            } else {
                try {
                    inputStream = new FileInputStream(location + resourcePath);
                } catch (FileNotFoundException e) {
                    inputStream = null;
                }
            }
            if (inputStream != null) {
                return inputStream;
            }
        }
        return null;
    }

    public boolean hasResource(HttpServletRequest req) {
        return getInputStream(req) != null;
    }

    public void add(String prefix, String location) {
        resourceMapping.put(prefix, location);
    }

    public void handle(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        // 获取资源后缀
        String extension = StringUtil.getExtension(WebUtil.getRequestURI(req));
        // 设置响应头
        resp.setContentType(MimeTypeEnum.getContentType(extension));

        InputStream inputStream = getInputStream(req);
        ServletOutputStream outputStream = resp.getOutputStream();

        IoUtil.copy(inputStream, outputStream);
    }

}
