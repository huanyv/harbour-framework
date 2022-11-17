package com.book.guard;

import top.huanyv.bean.annotation.Component;
import top.huanyv.webmvc.annotation.Guard;
import top.huanyv.webmvc.core.HttpRequest;
import top.huanyv.webmvc.core.HttpResponse;
import top.huanyv.webmvc.guard.NavigationGuard;

import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author huanyv
 * @date 2022/11/17 18:58
 */
@Component
@Guard(value = {"/**"}, exclude = {"/error/**", "/jquery/**", "/layui/**", "/login", "/admin/user/login"})
public class LoginGuard implements NavigationGuard {

    @Override
    public boolean beforeEach(HttpRequest req, HttpResponse resp) throws IOException {
        HttpSession session = req.getSession();
        Object username = session.getAttribute("username");
        if (username != null) {
            return true;
        }
        resp.redirect("/login");
        return false;
    }

}
