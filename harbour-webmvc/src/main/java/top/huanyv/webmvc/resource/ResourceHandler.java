package top.huanyv.webmvc.resource;

import top.huanyv.webmvc.config.WebMvcConstants;
import top.huanyv.webmvc.utils.WebUtil;
import top.huanyv.webmvc.config.ResourceMappingRegistry;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.*;

/**
 * @author admin
 * @date 2022/7/27 8:34
 */
public class ResourceHandler {

    private ResourceMappingRegistry resourceMappingRegistry;

    private final ResourceHolder resourceHolder;

    public ResourceHandler() {
        resourceHolder = new ResourceHolderManager();
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
        List<ResourceMapping> resourceMappings = resourceMappingRegistry.getResourceMappings();
        for (ResourceMapping mapping : resourceMappings) {
            String urlPattern = mapping.getUrlPattern();
            // 请求地址与pattern是否匹配
            if (WebMvcConstants.ANT_PATH_MATCHER.match(urlPattern, uri)) {
                // 获取通配符处的path
                String relativePath = WebMvcConstants.ANT_PATH_MATCHER.extractPathWithinPattern(urlPattern, uri);
                for (String location : mapping.getLocations()) {
                    inputStream = resourceHolder.getInputStream(location + relativePath);
                    if (inputStream != null) {
                        return inputStream;
                    }
                }
            }
        }
        return null;
    }

    public void setResourceMappingRegistry(ResourceMappingRegistry resourceMappingRegistry) {
        this.resourceMappingRegistry = resourceMappingRegistry;
    }

    @Override
    public String toString() {
        return resourceMappingRegistry.getResourceMappings().toString();
    }
}
