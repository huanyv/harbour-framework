package top.huanyv.webmvc.security;

import top.huanyv.webmvc.core.HttpRequest;
import top.huanyv.webmvc.core.HttpResponse;
import top.huanyv.webmvc.core.request.MethodRequestHandler;
import top.huanyv.webmvc.core.request.RequestHandler;
import top.huanyv.webmvc.guard.NavigationGuard;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 鉴权器
 *
 * @author huanyv
 * @date 2023/3/23 20:31
 */
public class Authorizer implements NavigationGuard {

    @Override
    public boolean beforeEach(HttpRequest req, HttpResponse resp, RequestHandler handler) throws Exception {
        if (handler instanceof MethodRequestHandler) {
            MethodRequestHandler methodHandler = (MethodRequestHandler) handler;
            // 获取控制器方法
            Method method = methodHandler.getMethod();
            // 是否用了权限注解
            HasPermission hasPermission = method.getAnnotation(HasPermission.class);
            if (hasPermission != null) {
                // 获取当前登录用户权限列表
                List<String> permissions = getPermissions(req, resp, handler);
                for (String permission : hasPermission.value()) {
                    if (permissions.contains(permission)) {
                        return true;
                    }
                }
                // 没有权限处理
                noPermissionHandle(req, resp);
                return false;
            }
        }
        return true;
    }

    public List<String> getPermissions(HttpRequest req, HttpResponse resp, RequestHandler handler) {
        StorageStrategy strategy = SubjectHolder.getStrategy();
        User subject = strategy.getSubject();
        return Arrays.asList(subject.getPermissions());
    }

    public void noPermissionHandle(HttpRequest req, HttpResponse resp) throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put("code", 403);
        map.put("msg", "没有权限");
        resp.json(map);
    }
}
