package top.huanyv.web.guard;

import top.huanyv.web.core.HttpRequest;
import top.huanyv.web.core.HttpResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * @author admin
 * @date 2022/7/27 16:07
 */
public class NavigationGuardChain {

    // 路由映射执行链
    private List<NavigationGuardMapping> navigationGuards = new ArrayList<>();

    public void setNavigationGuards(List<NavigationGuardMapping> navigationGuards) {
        this.navigationGuards = navigationGuards;
    }

    /**
     * 前置操作
     * @param request 请求对象
     * @param response 响应对象
     * @return 是否中断
     */
    public boolean handleBefore(HttpRequest request, HttpResponse response) {
        for (int i = 0; i < this.navigationGuards.size(); i++) {
            NavigationGuardMapping navigationGuardMapping = this.navigationGuards.get(i);
            boolean beforeEach = navigationGuardMapping.getNavigationGuard().beforeEach(request, response);
            if (!beforeEach) {
                return false;
            }
        }
        return true;
    }

    /**
     * 后置操作
     * @param request 请求对象
     * @param response 响应对象
     */
    public void handleAfter(HttpRequest request, HttpResponse response) {
        for (int i = this.navigationGuards.size() - 1; i >= 0; i--) {
            NavigationGuardMapping navigationGuardMapping = this.navigationGuards.get(i);
            navigationGuardMapping.getNavigationGuard().afterEach(request, response);
        }
    }
}
