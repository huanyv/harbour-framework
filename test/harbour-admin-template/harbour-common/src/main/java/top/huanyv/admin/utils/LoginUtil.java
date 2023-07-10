package top.huanyv.admin.utils;

import top.huanyv.webmvc.core.HttpRequest;
import top.huanyv.webmvc.security.SubjectHolder;
import top.huanyv.webmvc.security.User;
import top.huanyv.webmvc.utils.ServletHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author huanyv
 * @date 2023/3/31 21:57
 */
public class LoginUtil {

    public static User getLoginUser() {
        User user = SubjectHolder.getStrategy().getSubject();
        if (user == null) {
            throw new RuntimeException("no login.");
        }
        return user;
    }

    public static String getLoginUsername() {
        return getLoginUser().getUsername();
    }

    public static String getLoginUserId() {
        return getLoginUser().getId();
    }

}
