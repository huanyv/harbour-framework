package top.huanyv.webmvc.config;

import top.huanyv.webmvc.guard.NavigationGuard;
import top.huanyv.webmvc.guard.NavigationGuardMapping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author admin
 * @date 2022/7/28 16:13
 */
public class NavigationGuardRegistry {

    private List<NavigationGuardMapping> navigationGuards = new ArrayList<>();

    public NavigationGuardMapping addNavigationGuard(NavigationGuard navigationGuard) {
        NavigationGuardMapping navigationGuardMapping = new NavigationGuardMapping();
        navigationGuardMapping.setNavigationGuard(navigationGuard);
        this.navigationGuards.add(navigationGuardMapping);
        return navigationGuardMapping;
    }


    public List<NavigationGuardMapping> getConfigNavigationGuards() {
        return Collections.unmodifiableList(navigationGuards);
    }
}
