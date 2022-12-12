package top.huanyv.webmvc.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author admin
 * @date 2022/7/25 17:07
 */
public class ViewControllerRegistry {

    private Map<String, String> viewController = new HashMap<>();

    public ViewControllerRegistry add(String path, String viewName) {
        this.viewController.put(path, viewName);
        return this;
    }

    public Map<String, String> getViewController() {
        return Collections.unmodifiableMap(viewController);
    }
}
