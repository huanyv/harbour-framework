package top.huanyv.webmvc.config;

import top.huanyv.webmvc.resource.ResourceMapping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author admin
 * @date 2022/7/27 9:47
 */
public class ResourceMappingRegistry {

    private final List<ResourceMapping> resourceMappings = new ArrayList<>();

    /**
     * 添加资源，
     * <br><br>
     * <code>
     * registry.addResourceHandler("/static/**").addResourceLocations("classpath:static/")
     *                     .addResourceLocations("file:C:\\Users\\admin\\Desktop\\demo\\")
     * </code>
     * <br><br>
     * location中，后面的路径分隔符号必需加上
     * <br><br>
     * @param urlPattern urlpattern
     * @return mapping
     */
    public ResourceMapping addResourceHandler(String urlPattern) {
        ResourceMapping resourceMapping = new ResourceMapping();
        this.resourceMappings.add(resourceMapping);
        resourceMapping.setUrlPattern(urlPattern);
        return resourceMapping;
    }

    public List<ResourceMapping> getResourceMappings() {
        return Collections.unmodifiableList(resourceMappings);
    }
}
