package top.huanyv.admin.guard;

import top.huanyv.webmvc.core.HttpRequest;
import top.huanyv.webmvc.core.HttpResponse;
import top.huanyv.webmvc.core.request.RequestHandler;
import top.huanyv.webmvc.guard.NavigationGuard;
import top.huanyv.webmvc.security.SubjectHolder;
import top.huanyv.webmvc.security.User;

/**
 * @author huanyv
 * @date 2023/3/26 10:41
 */
public class AuthenticationGuard implements NavigationGuard {

    @Override
    public boolean beforeEach(HttpRequest req, HttpResponse resp, RequestHandler handler) throws Exception {
        User user = SubjectHolder.getStrategy().getSubject();
        if (user == null) {
            resp.redirect("/login");
            return false;
        }
        return true;
    }
}
