package top.huanyv.web.view;

import top.huanyv.enums.MimeTypeEnum;
import top.huanyv.utils.*;
import top.huanyv.web.config.WebMvcGlobalConfig;

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

    public static final String CLASSPATH_PREFIX = "classpath:";

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    private List<ResourceMapping> resourceMappings = new ArrayList<>();

    // 默认的静态资源前缀
    public ResourceHandler() {
        add("/**").addResourceLocations("classpath:static/");
    }


    /**
     * 从ServletContext中获取资源流
     * @param req 请求对象
     * @return 不存在， 从配置获取
     */
    public InputStream getInputStream(HttpServletRequest req) {
        String uri = WebUtil.getRequestURI(req);
        InputStream inputStream = req.getServletContext().getResourceAsStream(uri);
        if (inputStream == null) {
            inputStream = getInputStream(WebUtil.getRequestURI(req));
        }
        return inputStream;
    }


    /**
     * 从配置获取资源流，
     * @param uri 请求uri
     * @return 不存在返回null
     */
    public InputStream getInputStream(String uri) {
        InputStream inputStream = null;
        for (ResourceMapping mapping : this.resourceMappings) {
            String urlPattern = mapping.getUrlPattern();
            // 请求地址与pattern是否匹配
            if (pathMatcher.match(urlPattern, uri)) {
                // 获取通配符处的path
                String relativePath = pathMatcher.extractPathWithinPattern(urlPattern, uri);
                for (String location : mapping.getLocations()) {
                    if (location.startsWith(CLASSPATH_PREFIX)) {
                        inputStream = ClassLoaderUtil
                                .getInputStream(StringUtil.removePrefix(location, CLASSPATH_PREFIX) + relativePath);
                    } else {
                        try {
                            inputStream = new FileInputStream(location + relativePath);
                        } catch (FileNotFoundException e) {
                            inputStream = null;
                        }
                    }
                    if (inputStream != null) {
                        return inputStream;
                    }
                }
            }
        }
        return null;
    }

    /**
     * 是否存在这个资源
     * @param req 请求对象
     * @return bool
     */
    public boolean hasResource(HttpServletRequest req) {
        return getInputStream(req) != null;
    }

    /**
     * 添加一个资源映射
     * @param urlPattern pattern地址
     * @return mapping
     */
    public ResourceMapping add(String urlPattern) {
        ResourceMapping resourceMapping = new ResourceMapping();
        this.resourceMappings.add(resourceMapping);
        resourceMapping.setUrlPattern(urlPattern);
        return resourceMapping;
    }

    /**
     * 获取一个资源映射, 如果不存在新建一个
     * @return mapping
     */
    public ResourceMapping getMapping(String urlPattern) {
        for (ResourceMapping mapping : this.resourceMappings) {
            if (mapping.getUrlPattern().equals(urlPattern)) {
                return mapping;
            }
        }
        ResourceMapping resourceMapping = new ResourceMapping();
        resourceMapping.setUrlPattern(urlPattern);
        return resourceMapping;
    }

    /**
     * 添加一个资源映射， 如果已经存在对应的地址pattern，追加
     * @param mapping 映射对象
     */
    public void addMapping(ResourceMapping mapping) {
        ResourceMapping resourceMapping = getMapping(mapping.getUrlPattern());
        resourceMapping.addResourceLocations(mapping.getLocations().toArray(new String[0]));
        this.resourceMappings.add(resourceMapping);
    }

    /**
     * 批量添加多个资源映射
     * @param resourceMappings list集合
     */
    public void addMappings(List<ResourceMapping> resourceMappings) {
        for (ResourceMapping resourceMapping : resourceMappings) {
            addMapping(resourceMapping);
        }
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

    @Override
    public String toString() {
        return "ResourceHandler{" +
                "resourceMappings=" + resourceMappings +
                '}';
    }
}
