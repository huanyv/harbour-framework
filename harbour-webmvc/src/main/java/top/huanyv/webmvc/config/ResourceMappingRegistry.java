package top.huanyv.webmvc.config;

import top.huanyv.webmvc.resource.ResourceMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * @author admin
 * @date 2022/7/27 9:47
 */
public class ResourceMappingRegistry {

    private List<ResourceMapping> resourceMappings = new ArrayList<>();

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

    public List<ResourceMapping> getResourceMappings() {
        return resourceMappings;
    }
}
