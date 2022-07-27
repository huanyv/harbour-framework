package top.huanyv.web.config;

import java.util.HashMap;
import java.util.Map;

/**
 * @author admin
 * @date 2022/7/27 9:47
 */
public class ResourceMappingRegistry {
    private Map<String, String> resourceMapping = new HashMap<>();

    public ResourceMappingRegistry add(String prefix, String location) {
        resourceMapping.put(prefix, location);
        return this;
    }

    public Map<String, String> getResourceMapping() {
        return resourceMapping;
    }
}
