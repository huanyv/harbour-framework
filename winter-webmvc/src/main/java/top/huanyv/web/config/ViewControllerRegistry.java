package top.huanyv.web.config;

import top.huanyv.web.view.ViewResolver;

import java.util.HashMap;
import java.util.Map;

/**
 * @author admin
 * @date 2022/7/25 17:07
 */
public class ViewControllerRegistry {

    private Map<String, String> viewController = new HashMap<>();

    private ViewResolver viewResolver;

    public ViewControllerRegistry add(String path, String viewName) {
        this.viewController.put(path, viewName);
        return this;
    }

    public void setViewResolver(ViewResolver viewResolver) {
        this.viewResolver = viewResolver;
    }

    public Map<String, String> getViewController() {
        return viewController;
    }
}
