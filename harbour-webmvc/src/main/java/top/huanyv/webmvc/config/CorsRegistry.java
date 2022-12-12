package top.huanyv.webmvc.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author admin
 * @date 2022/8/4 16:22
 */
public class CorsRegistry {
    private List<CorsRegistryBean> corsRegistryBeans = new ArrayList<>();

    public CorsRegistryBean addMapping(String pattern) {
        CorsRegistryBean corsRegistryBean = new CorsRegistryBean(pattern);
        this.corsRegistryBeans.add(corsRegistryBean);
        return corsRegistryBean;
    }

    public List<CorsRegistryBean> getCorsRegistryBeans() {
        return Collections.unmodifiableList(corsRegistryBeans);
    }
}
