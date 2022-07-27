package top.huanyv.web.guard;

import top.huanyv.utils.AntPathMatcher;
import top.huanyv.web.core.HttpRequest;
import top.huanyv.web.core.HttpResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author admin
 * @date 2022/7/27 16:07
 */
public class NavigationGuardChain {

    private List<NavigationGuardMapping> navigationGuards = new ArrayList<>();

    public void setNavigationGuards(List<NavigationGuardMapping> navigationGuards) {
        this.navigationGuards = navigationGuards;
    }


    public boolean handleBefore(HttpRequest request, HttpResponse response) {
        List<NavigationGuardMapping> navigationGuardMappings = this.navigationGuards.stream()
                .sorted((o1, o2) -> o1.getOrder() - o2.getOrder())
                .collect(Collectors.toList());
        for (NavigationGuardMapping navigationGuardMapping : navigationGuardMappings) {
            boolean beforeEach = navigationGuardMapping.getNavigationGuard().beforeEach(request, response);
            if (!beforeEach) {
                return false;
            }
        }
        return true;
    }
    public void handleAfter(HttpRequest request, HttpResponse response) {
        List<NavigationGuardMapping> navigationGuardMappings = this.navigationGuards.stream()
                .sorted((o1, o2) -> o2.getOrder() - o1.getOrder())
                .collect(Collectors.toList());
        for (NavigationGuardMapping navigationGuardMapping : navigationGuardMappings) {
            navigationGuardMapping.getNavigationGuard().afterEach(request, response);
        }

    }

}
